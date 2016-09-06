package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.deploy.FilterDef;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by tisong on 9/3/16.
 */
public class StandardContext
    extends ContainerBase
    implements Context{

    private boolean available = false;

    private String displayName = null;

    private String docBase = null;

    private String workDir = null;


    private Map envs = new HashMap();

    /**
     * name - FilterDef
     */
    private Map<String, FilterDef> filterDefs = new HashMap<String, FilterDef>();
    /**
     * name - ApplicationFilterConfig
     */
    private Map filterConfigs = new HashMap();



    private String mapperClass = "org.apache.catalina.core.StandardContextMapper";

    private String wrapperClass = "org.apache.catalina.core.StandardWrapper";


    /**
     * pattern - name
     */
    private Map<String, String> servletMappings = new HashMap<String, String>();
    /**
     * name - wrapper
     */
    private Map<String, Wrapper> children = new HashMap<String, Wrapper>();


    private int sessionTimeout = 30;


    private String[] applicationListeners = new String[0];
    private Object[] applicationListenersObjects = new Object[0];





    private ApplicationContext context =  null;



    public StandardContext() {

        super();
        pipeline.setBasic(new StandardContextValve());
    }



    @Override
    public void addLoader(Loader loader) {

    }

    @Override
    public Loader getLoader() {
        return null;
    }




    @Override
    public void addServletMapping(String pattern, String name) {

        servletMappings.put(pattern, name);

    }

    @Override
    public String findServletMapping(String pattern) {
        return null;
    }

    @Override
    public String[] findServletMappings() {
        return new String[0];
    }


    @Override
    public boolean isAvailable() {
        return this.available;
    }

    @Override
    public void setAvailable(boolean available) {

        this.available = available;
    }

    @Override
    public String getDocBase() {
        return null;
    }

    @Override
    public void setDocBase(String docBase) {

    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setPath(String path) {

    }

    @Override
    public boolean getReloadable() {
        return false;
    }

    @Override
    public void setReloadable(boolean reloadable) {

    }

    @Override
    public int getSessionTimeout() {
        return 0;
    }

    @Override
    public void setSessionTimeout(int timeout) {

    }

    @Override
    public Object[] getApplicationListenersObjects() {
        return applicationListenersObjects;
    }

    @Override
    public void setApplicationListeners(String[] applicationListeners) {
        this.applicationListeners = applicationListeners;
    }



    @Override
    public ServletContext getServletContext() {


        if (context == null) {
            context = new ApplicationContext(getBasePath(), this);
        }

        return context;
    }


    /**
     * 设置 resources
     * 1. 启动Loader
     * 2. 启动Manager
     * 3. 设置 ServletContext 属性
     * 4. JNDI 资源初始化
     * 5. 监听器初始化
     * 6. 过滤器初始化
     * @throws LifecycleException
     */
    public void start() throws LifecycleException {


        setAvailable(false);
        //setConfigured(false);

        if (getResources() == null) {

            if (docBase != null && docBase.endsWith(".war")) {
                setResources(new WARDirContext());
            } else {
                setResources(new FileDirContext());
            }
        }

        if (getLoader() == null) {
            setLoader(new WebappLoader(getParentClassLoader()));
        }

        if (getManager() == null) {
            setManager(new StandardManager());
        }


        postWorkDirectory();

        super.start();


        createMamingContext();

        listenerStart();

        filterStart();

        postWelcomeFiles();


        setAvailable(true);
    }


    /**
     * 设置 ServletContext 属性
     * eg: javax.servlet.context.tempdir
     */
    private void postWorkDirectory() {


        String workDir = this.workDir;
        if (workDir == null) {

        }


        File dir = new File(workDir);
        if (!dir.isAbsolute()) {
            // 如果不是绝对路径, 则要转变为绝对路径
            File catalinaHome = new File(System.getProperty("catalina.base"));
            String catalinaHomePath = null;
            try {
                catalinaHomePath = catalinaHome.getCanonicalPath();
                dir = new File(catalinaHomePath, workDir);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        dir.mkdirs();

        this.getServletContext().setAttribute(Globals.WORK_DIR_ATTR, dir);
        if (this.getServletContext() instanceof ApplicationContext) {
            ((ApplicationContext) getServletContext()).setAttributeReadOnly
                    (Globals.WORK_DIR_ATTR);
        }

    }

    /**
     * 将 FitlerDef 转化为 ApplicationFilterConfig(封装了一个Filter对象)
     * @return
     */
    public boolean filterStart() {

        boolean ok = true;

        filterConfigs.clear();

        Iterator<String> names = filterDefs.keySet().iterator();
        while (names.hasNext()) {

            String name = names.next();
            ApplicationFilterConfig filterConfig = null;
            try {
                filterConfig = new ApplicationFilterConfig(
                        this, filterDefs.get(name));
                filterConfigs.put(name, filterConfig);
            } catch (Throwable t) {
                t.printStackTrace();
                ok = false;
            }
        }

        return ok;
    }

    /**
     * 创建 Listener对象
     * @return
     */
    public boolean listenerStart() {

        String[] listeners = findApplicationListeners();
        Object[] results = new Object[listeners.length];

        for (String listener: listeners) {

        }

        setApplicationListeners(results);
        Object[] instances = getApplicationListenersObjects();


        ServletContextEvent event = new ServletContextEvent(getServletContext());
        for (Object instance : instances) {

            ServletContextListener listener = (ServletContextListener) instance;

            listener.contextInitialized(event);
        }

        return ok;
    }


    private void postWelcomeFiles() {
        getServletContext().setAttribute("org.apache.catalina.WELCOME_FILES",
                welcomeFiles);

    }



    public void loadOnStartUp(Container[] children) {

    }
}
