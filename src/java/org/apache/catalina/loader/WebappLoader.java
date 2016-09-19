package org.apache.catalina.loader;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.naming.resources.Resource;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.servlet.ServletContext;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * Created by tisong on 9/15/16.
 */
public class WebappLoader implements Loader, Lifecycle, Runnable{

    private int checkInterval = 15;



    private Container container = null;


    private ClassLoader parentClassLoader = null;

    private String loaderClass = "org.apache.catalina.loader.WebappClassLoader";

    private WebappClassLoader classLoader = null;


    private String[] repositories = new String[0];



    private boolean delegate = false;

    private boolean reloadable = false;

    private boolean started = false;



    protected LifecycleSupport lifecycleSupport = new LifecycleSupport(this);



    private Thread thread = null;

    private boolean threadDone = false;

    private String threadName = "WebappLoader";





    public WebappLoader(ClassLoader parentClassLoader) {

        this.parentClassLoader = parentClassLoader;
    }



    @Override
    public ClassLoader getClassLoader() {
        return this.classLoader;
    }

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public boolean getDelegate() {
        return false;
    }

    @Override
    public void setDelegate(boolean delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean getReloadable() {
        return this.reloadable;
    }

    @Override
    public void setReloadable(boolean reloadable) {
        this.reloadable = reloadable;
    }

    @Override
    public boolean modified() {
        return classLoader.modified();
    }

    @Override
    public void addRepository(String repository) {

    }

    @Override
    public String[] findRepositories() {
        return new String[0];
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }


    @Override
    public void start() throws LifecycleException {

        try {
            classLoader = createClassLoader();
            classLoader.setResources(container.getResources());

            classLoader.setDelegate(delegate);

            for (String repository: repositories) {
                classLoader.addRepository(repository);
            }

            setRepositories();
            setClassPath();


            //setPermissions();

            if (classLoader instanceof Lifecycle) {
                classLoader.start();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        if (reloadable) {

            threadStart();
        }
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



    private WebappClassLoader createClassLoader() throws Exception{

        // TODO 为什么要用反射的机制去创建 WebappClassLoader呢;
        // TODO 方便更改把; 比如 loaderClass改变的代码是不用改变的

        WebappClassLoader webappClassLoader = null;
        Class clazz = Class.forName(loaderClass);

        if (parentClassLoader == null) {
            webappClassLoader = (WebappClassLoader)clazz.newInstance();
        } else {
            Class[] argTypes = {ClassLoader.class};
            Object[] objs = {parentClassLoader};

            Constructor constructor = clazz.getConstructor(argTypes);
            webappClassLoader = (WebappClassLoader)constructor.newInstance(objs);
        }

        return webappClassLoader;
    }





    private void setRepositories() {

        ServletContext servletContext = ((Context) container).getServletContext();


        // TODO where
        File workDir = (File) servletContext.getAttribute(Globals.WORK_DIR_ATTR);

        DirContext resources = container.getResources();



        DirContext classes = null;
        String classesPath = "/WEB-INF/classes";
        try {

            Object object = resources.lookup(classesPath);
            if (object instanceof DirContext) {
                classes = (DirContext) object;
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }

        if (classes != null) {

            File classRepository = null;

            String absoluteClassesPath = servletContext.getRealPath(classesPath);

            if (absoluteClassesPath != null) {
                classRepository = new File(absoluteClassesPath);
            } else {
                System.out.println("webapp loader set repository absolute classes path is null");
            }
//            } else {
//                classRepository = new File(workDir, classesPath);
//                classRepository.mkdirs();
//                copyDir(classes, classRepository);
//            }

            classLoader.addRepository(classesPath + "/", classRepository);
        }




        DirContext libDir = null;

        String libPath = "/WEB-INF/lib";
        classLoader.setJarPath(libPath);

        try {
            Object object = resources.lookup(libPath);
            if (object instanceof DirContext) {
                libDir = (DirContext) object;
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }

        if (libDir != null) {

            boolean copyJars = false;
            String absoluteLibPath = servletContext.getRealPath(libPath);

            File destDir = null;

            if (absoluteLibPath != null) {
                destDir = new File(absoluteLibPath);
            }

            // Looking up directory /WEB-INF/lib in the context

            try {
                NamingEnumeration namingEnumeration = resources.listBindings(libPath);
                while (namingEnumeration.hasMoreElements()) {
                    Binding binding= (Binding) namingEnumeration.nextElement();

                    String fileName = libPath + "/" + binding.getName();
                    if (!fileName.endsWith(".jar")) {
                        continue;
                    }

                    File destFile = new File(destDir, binding.getName());

                    Resource jarResource = (Resource) binding.getObject();

                    JarFile jarFile = new JarFile(destFile);
                    classLoader.addJar(fileName, jarFile, destFile);
                }
            } catch (NamingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void setClassPath() {

        ServletContext servletContext = ((Context) container).getServletContext();
        if (servletContext == null) {
            return ;
        }

        StringBuffer classpath = new StringBuffer();

        ClassLoader loader = getClassLoader();

        int layers = 0;
        int n = 0;
        while (loader != null) {

            URL[] repositories = ((URLClassLoader) loader).getURLs();

            for (URL repository: repositories) {
                String res = repository.toString();

                if (n > 0) {
                    classpath.append(File.pathSeparator);
                }
                classpath.append(repository);
                n++;
            }

            loader = loader.getParent();
            layers++;
        }

        servletContext.setAttribute(Globals.CLASS_PATH_ATTR,
                classpath.toString());
    }

    private boolean copyDir(DirContext srcDir, File destDir) {

        return true;
    }






    public void run() {

        while (!threadDone) {

            threadSleep();

            if (! classLoader.modified()) {
                continue;
            }


            notifyContext();
            break;
        }
    }


    private void threadStart() {

        threadDone = false;
        threadName = "WebappLoader[" + container.getName() + "]";
        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }


    private void threadSleep() {

        try {
            Thread.sleep(checkInterval * 1000L);
        } catch (InterruptedException e) {
            ;
        }
    }


    private void threasStop() {


    }

    private void notifyContext() {

        WebappContextNotifier notifier = new WebappContextNotifier();
        (new Thread(notifier)).start();

    }


    protected class WebappContextNotifier implements Runnable {

        @Override
        public void run() {
            ((Context) container).reload();
        }
    }
}
