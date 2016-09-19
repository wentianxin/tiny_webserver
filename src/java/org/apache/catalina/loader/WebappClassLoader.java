package org.apache.catalina.loader;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.naming.resources.Resource;
import org.apache.naming.resources.ResourceAttributes;

import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

/**
 * Created by tisong on 9/18/16.
 */
public class WebappClassLoader extends URLClassLoader
    implements Reloader, Lifecycle{


    protected Map<String, ResourceEntry> resourceEntires = new HashMap<String, ResourceEntry>();


    private ClassLoader parent = null;

    private ClassLoader system = null;


    protected DirContext resources = null;


    protected Map resourceEntries = new HashMap();


    protected String[] repositories = new String[0];
    protected File[] files = new File[0];



    protected JarFile[] jarFiles = new JarFile[0];

    protected File[] jarRealFiles = new File[0];

    protected String jarPath = null;

    protected String[] jarNames = new String[0];



    protected String[] paths = new String[0];
    protected long[] lastModifiedDates = new long[0];



    protected boolean delegate = false;
    protected boolean hasExternalRepositories = false;


    public WebappClassLoader() {

        super(new URL[0]);
        this.parent = getParent();
        system = getSystemClassLoader();
//        securityManager = System.getSecurityManager();
//
//        if (securityManager != null) {
//            refreshPolicy();
//        }

    }


    /**
     * Construct a new ClassLoader with no defined repositories and no
     * parent ClassLoader.
     */
    public WebappClassLoader(ClassLoader parent) {

        super(new URL[0], parent);
        this.parent = getParent();
        system = getSystemClassLoader();
//        securityManager = System.getSecurityManager();
//
//        if (securityManager != null) {
//            refreshPolicy();
//        }

    }


    /**
     * 只要有一个 class 文件被修改，重新加载整个
     * @return
     */
    public boolean modified() {

        int length = paths.length;

        for (int i = 0; i < length; i++) {
            try {
                long lastModified = ( (ResourceAttributes) resources.getAttributes(paths[i]) ).getLastModified();
                if (lastModified != lastModifiedDates[i]) {
                    return true;
                }
            } catch (NamingException e) {
                // 资源丢失
                return true;
            }
        }

        length = jarNames.length;

        /**
         * Jar 包检查: 两种情况
         * 1. Jar包增加文件
         * 2. Jar包缺少文件
         */
        if (getJarPath() != null) {

        }

        return false;
    }



    public Class loadClass(String name) throws ClassNotFoundException {

        return loadClass(name, false);
    }


    /**
     * 1. 先查找缓存
     * 2. 使用系统类加载器
     * 3. 安全管理器
     * 4. 判断是否使用了委托标志; Y: 交给父类加载, N: 从当前repository中加载
     * 5. 若未从当前源中找到, 则无条件的交给父类加载器加载
     * @param name 类名
     * @param resolve
     * @return
     */
    public Class loadClass(String name, boolean resolve) throws ClassNotFoundException {

        Class clazz = null;

        /**
         * Step 1: 查找本地缓存
         */

        /**
         * Step 2: 调用ClassLoader类的 findLoaderClass方法查找缓存
         */
        clazz = findLoadedClass(name);
        if (clazz != null) {

        }

        /**
         * Step 3: 使用系统类加载器
         */
        try {
            clazz = system.loadClass(name);
            if (clazz != null) {
                return clazz;
            }
        } catch (ClassNotFoundException e) {

        }


        /**
         *
         */


        /**
         * Step 4: 委托标志的判断
         */
        boolean delegatedLoad = delegate;

        if (delegatedLoad) {

            ClassLoader loader = ( (parent == null) ? system: parent );

            try {
                loader.loadClass(name);
            } catch (ClassNotFoundException e) {

            }
        }

        /**
         * Step 5: 从当前源中查找
         */
        try {
            clazz = findClass(name);
        } catch (ClassNotFoundException e) {

        }

        /**
         * Step 6: 如果未被委托, 且又没有在当前 repository 中找到, 则交给父类加载器
         */
        if (!delegatedLoad) {
            ClassLoader loader = ((parent == null) ? system : parent );
            try {
                clazz = loader.loadClass(name);
                if (clazz != null) {
                    return clazz;
                }
            } catch (ClassNotFoundException e) {

            }
        }

        throw new ClassNotFoundException(name);
    }



    public Class findClass(String name) throws ClassNotFoundException {

        return findClassInternal(name);
    }



    public URL[] getURLS() {

        return null;
    }


    /**
     * 最后调用的还是 URLClassLoader addURL方法
     * @param repository
     */
    public void addRepository(String repository) {

        if (repository.startsWith("/WEB-INF/lib")
                || repository.startsWith("/WEB-INF/classes"))
            return;

        // Add this repository to our underlying class loader
        try {
            URL url = new URL(repository);
            super.addURL(url);
            hasExternalRepositories = true;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.toString());
        }
    }


    /**
     * 对两个数组的扩容
     */
    void addRepository(String repository, File file) {

        String[] result = new String[repositories.length + 1];
        for (int i = 0; i < repositories.length; i++) {
            result[i] = repositories[i];
        }
        result[repositories.length] = repository;
        repositories = result;

        // Add the file to the list
        File[] result2 = new File[files.length + 1];
        for (int i = 0; i < files.length; i++) {
            result2[i] = files[i];
        }
        result2[files.length] = file;
        files = result2;

    }



    public void addJar(String jar, JarFile jarFile, File file) {

        /**
         * 扩充 jarNames 数组
         */
        if ((jarPath != null) && (jar.startsWith(jarPath))) {

            String jarName = jar.substring(jarPath.length());

            while (jarName.startsWith("/")) {
                jarName = jarName.substring(1);
            }

            String[] result = new String[jarNames.length + 1];
            for (int i = 0; i < jarNames.length; i++) {
                result[i] = jarNames[i];
            }
            result[jarNames.length] = jarName;
            jarNames = result;
        }


        JarFile[] result2 = new JarFile[jarFiles.length + 1];
        for (int i = 0; i < jarFiles.length; i++) {
            result2[i] = jarFiles[i];
        }
        result2[jarFiles.length] = jarFile;
        jarFiles = result2;


        File[] result4 = new File[jarRealFiles.length + 1];
        for (int i = 0; i < jarRealFiles.length; i++) {
            result4[i] = jarRealFiles[i];
        }
        result4[jarRealFiles.length] = file;
        jarRealFiles = result4;


    }


    public String getJarPath() {

        return null;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    protected Class findLoacalClass(String name) {

        ResourceEntry entry = resourceEntires.get(name);
        if (entry != null) {
            return entry.loadedClass;
        }

        return null;
    }


    protected Class findClassInternal(String name) {

        Class clazz = null;

        return clazz;
    }



    protected ResourceEntry findResourceInternal(String name, String path) {



        int contentLength = -1;
        InputStream binaryStream = null;

        int jarFilesLength = jarFiles.length;
        int repositoriesLength = repositories.length;


        ResourceEntry entry = (ResourceEntry) resourceEntries.get(name);
        if (entry != null)
            return entry;

        Resource resource = null;

        for (int i = 0; i < repositories.length; i++) {

            String repository = repositories[i];

            try {
                String fullPath = repository + path;

                Object lookupResult = resources.lookup(fullPath);
                if (lookupResult instanceof Resource) {
                    resource = (Resource) lookupResult;
                }


                entry = new ResourceEntry();

                try {
                    entry.source = getURL(new File(files[i], path));
                    entry.codeBase = entry.source;
                }  catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                /**
                 * 获取资源的属性(倘若是文件，即是文件的属性)
                 */
                ResourceAttributes attributes = (ResourceAttributes) resources.getAttributes(fullPath);

                contentLength = (int) attributes.getContentLength();
                entry.lastModified = attributes.getLastModified();



                if (resource != null) {
                    try {
                        binaryStream = resource.streamContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NamingException e) {

            }
        }



        if (binaryStream != null) {

            byte[] binaryContent = new byte[contentLength];

            entry.binaryContent = binaryContent;
        }

        return entry;
    }




    /**
     * 将 File 转化为 URL
     */
    protected URL getURL(File file) throws MalformedURLException {

        // TODO

        File realFile = file;

        try {
            realFile = realFile.getCanonicalFile();
        } catch (IOException e) {

        }

        return realFile.toURL();
    }


    public void setResources(DirContext resources) {
        this.resources = resources;
    }

    public DirContext getResources() {
        return resources;
    }

    public void setDelegate(boolean delegate) {
        this.delegate = delegate;
    }

    public boolean isDelegate() {
        return delegate;
    }




    @Override
    public void start() throws LifecycleException {

    }

    @Override
    public void stop() throws LifecycleException {

    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }



}
