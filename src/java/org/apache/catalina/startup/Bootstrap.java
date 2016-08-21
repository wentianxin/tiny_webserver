package org.apache.catalina.startup;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.Globals;
import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.apache.catalina.startup.ClassLoaderFactory.RepositoryType;
/**
 * Created by tisong on 8/16/16.
 */
public class Bootstrap {

    private static Bootstrap daemon;


    private Object catalinaDaemon = null;


    public static void main(String[] args) {

        if (daemon == null) {
            // TODO
            Bootstrap bootstrap = new Bootstrap();
            try {
                bootstrap.init();
            } catch (Throwable t) {
                handleThrowable(t);
                t.printStackTrace();
                return;
            }
            daemon = bootstrap;
        } else {

        }

        try {
            String command = "start";

            if (command.equals("startd")) {

            } else if (command.equals("start")) {
                daemon.setAwait(true);
                daemon.load(args);
                daemon.start();
            } else if (command.equals("stopd")) {

            } else if (command.equals("stop")) {

            } else if (command.equals("configtest")) {

            } else {

            }
        } catch (Throwable t) {

        }

    }


    /* common.loader=${catalina.home}/lib,${catalina.home}/lib/*.jar */
    private ClassLoader commonLoader = null;
    private ClassLoader catalinaLoader = null;
    private ClassLoader sharedLoader = null;

    private void initClassLoaders() {

        try {
            commonLoader = createClassLoader("common", null);

            catalinaLoader = createClassLoader("server", commonLoader);

            sharedLoader = createClassLoader("shared", commonLoader);
        } catch (Throwable t) {

        }
    }

    private ClassLoader createClassLoader(String name, ClassLoader parent) throws Exception {


        List<Repository> repositories = new ArrayList<Repository>();

        ClassLoader classLoader = ClassLoaderFactory.createClassLoader(repositories, parent);

        return classLoader;
    }

    public void init() throws Exception {

        /* 初始化 catalina(tomcat) 工作目录 */
        setCatalinaHome();
        setCatalinaBase();


        initClassLoaders();


        // TODO
        /*
            Thread.currentThread().setContextClassLoader(catalinaLoader);

             SecurityClassLoad.securityClassLoad(catalinaLoader);
         */


        Class<?> startupClass = catalinaLoader.loadClass("org.apache.catalina.startup.Catalina");

        Object startupInstance = startupClass.newInstance();

        /*
            基于反射机制调用 Catalina 方法: setParentClassLoader
         */
        String methodName = "setParentClassLoader";
        /* 参数类型 */
        Class<?>[] paramTypes = new Class[1];
        paramTypes[0] = Class.forName("java.lang.ClassLoader");
        /* 参数值 */
        Object[] paramValues = new Object[1];
        paramValues[0] = sharedLoader;

        Method method = startupInstance.getClass().getMethod(methodName, paramTypes);
        method.invoke(startupInstance, paramValues);

        catalinaDaemon = startupInstance;
    }

    /**
     * load catalinaDaemon by Class
     * @param arguments
     * @throws Exception
     */
    public void load(String[] arguments) throws Exception {

        String methodName = "load";
        Object[] param;
        Class<?>[] paramTypes;

        if (arguments==null || arguments.length==0) {
            paramTypes = null;
            param = null;
        } else {
            paramTypes = new Class[1];
            paramTypes[0] = arguments.getClass();
            param = new Object[1];
            param[0] = arguments;
        }

        Method method =
                catalinaDaemon.getClass().getMethod(methodName, paramTypes);
        method.invoke(catalinaDaemon, param);
    }

    public void start() throws Exception {

        Method method = catalinaDaemon.getClass().getMethod("start", null);

        method.invoke(catalinaDaemon, null);
    }


    public void setAwait(boolean await) throws Exception {
        Class<?>[] paramTypes = new Class[1];
        paramTypes[0] = Boolean.TYPE;

        Object[] paramValues = new Object[1];
        paramValues[0] = Boolean.valueOf(await);

        Method method = catalinaDaemon.getClass().getMethod("setAwait", paramTypes);

        method.invoke(catalinaDaemon, paramValues);
    }


    private void setCatalinaBase() {

        if (System.getProperty(Globals.CATALINA_BASE_PROP) != null)
            return;
        if (System.getProperty(Globals.CATALINA_HOME_PROP) != null)
            System.setProperty(Globals.CATALINA_BASE_PROP,
                    System.getProperty(Globals.CATALINA_HOME_PROP));
        else
            System.setProperty(Globals.CATALINA_BASE_PROP,
                    System.getProperty("user.dir"));

    }


    private void setCatalinaHome() {

        if ( System.getProperty(Globals.CATALINA_HOME_PROP) != null ) {
            return ;
        }

        File file = new File(System.getProperty("user.dir"), "bootstrap.jar");
        if (file.exists()) {
            // 即当前工作路径是 tomcat/bin/

            try {
                System.setProperty(Globals.CATALINA_HOME_PROP,
                        (new File(System.getProperty("user.dir"), "..")).getCanonicalPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 当前工作目录是 tomcat/
            System.setProperty(Globals.CATALINA_HOME_PROP, System.getProperty("user.dir"));
        }
    }

    public static String getCatalinaHome() {
        return System.getProperty(Globals.CATALINA_HOME_PROP,
                System.getProperty("user.dir"));
    }


}
