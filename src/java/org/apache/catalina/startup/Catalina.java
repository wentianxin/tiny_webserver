package org.apache.catalina.startup;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.Socket;

/**
 * Catalina 的启动过程
 * 1. 设置 catalina home&base 工作目录
 * 2. 解析 server.xml 配置文件
 * 3. 初始化 JNDI
 * 4. 增加 CatalinaShutdown
 * 4. 启动 server
 * 5. 等待 停止命名
 * Created by tisong on 9/6/16.
 */
public class Catalina {

    /**
     * 配置文件的相对路径(相对于catalina.home)
     */
    protected String configFile = "conf/server.xml";

    /**
     * 声明周期的状态标志
     */
    protected boolean starting = false;
    protected boolean stopping = false;

    /**
     * 是否开启JNDI资源
     */
    protected boolean useNaming = true;

    /**
     * 相关联的 Server服务器
     */
    protected Server server = null;

    /**
     * The shared extensions class loader for this server.
     */
    protected ClassLoader parentClassLoader = ClassLoader.getSystemClassLoader(); // 系统类加载器


    /**
     * 执行流程 main - process - execute - start / stop
     */
    public static void main(String[] args){
        new Catalina().process(args);
    }


    public void process(String[] args) {

        /**
         * 设置系统变量: catalina.home usr.dir(即为用户的当前目录)
         */
        setCatalinaHome();
        setCatalinaBase();

        try {
            if (arguments(args)) {
                execute();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    protected void execute() throws Exception {

        if (starting) {
            start();
        } else if (stopping) {
            stop();
        }
    }


    protected void start() {

        Digester digester = createStartDigester();

        File file = configFile();

        FileInputStream fis = null;
        try {
            InputSource is = new InputSource("file://" + file.getAbsolutePath());

            fis = new FileInputStream(file);

            is.setByteStream(fis);

            digester.push(this);
            digester.parse(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!useNaming) {
            System.setProperty("catalina.useNaming", "false");
        } else {
            System.setProperty("catalina.useNaming", "true");
            String value = "org.apache.naming";
            String oldValue =
                    System.getProperty(javax.naming.Context.URL_PKG_PREFIXES);
            if (oldValue != null) {
                value = value + ":" + oldValue;
            }
            System.setProperty(javax.naming.Context.URL_PKG_PREFIXES, value);
            value = System.getProperty
                    (javax.naming.Context.INITIAL_CONTEXT_FACTORY);
            if (value == null) {
                System.setProperty
                        (javax.naming.Context.INITIAL_CONTEXT_FACTORY,
                                "org.apache.naming.java.javaURLContextFactory");
            }
        }


        // Setting additional variables
        // System.getProperty(javax.naming.Context.URL_PKG_PREFIXES;

        Thread shutdownHook = new CatalineShutdownHook();

        if (server instanceof Lifecycle) {
            try {
                server.initialize();
                ((Lifecycle) server).start();

                Runtime.getRuntime().addShutdownHook(shutdownHook);

                server.await();
            } catch (LifecycleException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (server instanceof Lifecycle) {
            Runtime.getRuntime().removeShutdownHook(shutdownHook);
            try {
                ((Lifecycle) server).stop();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }
    }

    protected void stop() {

        /**
         * 创建的 Server 对象调用 await方法监听某个端口的命令
         * 所以要创建一个新的 Server 对象来向之前的 Server对象发送停止命令
         */
        Digester digester = createStopDigetster();
        File file = configFile();

        try {
            InputSource is = new InputSource("file://" + file.getAbsolutePath());
            FileInputStream fis = new FileInputStream(file);
            digester.push(this);
            digester.parse(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        try {
            Socket socket = new Socket("127.0.0.1", server.getPort());
            OutputStream stream = socket.getOutputStream();
            String shutdown = server.getShutdown();
            for (int i = 0; i < shutdown.length(); i++)
                stream.write(shutdown.charAt(i));
            stream.flush();
            stream.close();
            socket.close();
        } catch (IOException e) {
            System.out.println("Catalina.stop: " + e);
            e.printStackTrace(System.out);
            System.exit(1);
        }
    }



    /**
     * 获取配置文件(conf/server.xml)
     */
    private File configFile() {
        File file = new File(configFile);

        return file;
    }


    /**
     * 根据输入参数设置相应状态和变量(这里可读取参数中的 configFile 路径覆盖默认的路径)
     */
    private boolean arguments(String[] args) {

        boolean isConfig = false;

        if (args.length < 1) {
            usage();
            return false;
        }

        for (String s: args) {
            if (isConfig) {
                configFile = s;
                isConfig = false;
            } else if (s.equals("-config")) {
                isConfig = true;
            } else if (s.equals("-help")) {
                usage();
                return false;
            } else if (s.equals("-nonaming")) {
                useNaming = false;
            } else if (s.equals("start")) {
                starting = true;
            } else if (s.equals("stop")) {
                stopping = true;
            } else {
                usage();
                return false;
            }
        }

        return true;
    }

    /**
     * 输出 catalina正确的启动参数
     */
    protected void usage() {

        System.out.println
                ("usage: java org.apache.catalina.startup.Catalina"
                        + " [ -config {pathname} ] [ -debug ]"
                        + " [ -nonaming ] { start | stop }");

    }


    /**
     * 设置系统变量 catalina.home(catalina的工作目录) 为 user.dir(即启动程序时的目录)
     */
    protected void setCatalinaHome() {

        if (System.getProperty("catalina.home") != null) {
            return;
        }
        System.setProperty("catalina.home",
                System.getProperty("user.dir"));

    }

    /**
     * 设置系统变量 catalina.base 为 catalina.home
     */
    protected void setCatalinaBase() {

        if (System.getProperty("catalina.base") != null) {
            return;
        }
        System.setProperty("catalina.base",
                System.getProperty("catalina.home"));

    }


    /**
     * Server
     *  GlobalNamingResources
     *  Listener
     *  Service
     *      Listener
     *      Connector
     *          Facotry
     *          Listener
     *      Engine
     *          Default
     *          DefaultContext
     *      Host
     *          Default
     *          DefaultContext
     */
    protected Digester createStartDigester() {


        /**
         * Server Service Engine Host
         */
        Digester digester = new Digester();

        digester.setValidating(false);


        /**
         * Server服务器的实例化
         */
        digester.addObjectCreate("Server",
                                 "org.apache.core.StandardServer",
                                 "className");
        digester.addSetProperties("Server");

        /**
         * Server 全局资源的实例化
         **/
        digester.addObjectCreate("Server/GlobalNamingResources",
                                 "org.apache.catalina.deploy.NamingResources");
        digester.addSetProperties("Server/GlobalNamingResources");
        digester.addSetNext("Server/GlobalNamingResources",
                            "setGlobalNamingResources");
        digester.addRuleSet(new NamingRuleSet("Server/GlobalNamingResources/"));

        /**
         * Server 监听器实例化
         */
        digester.addObjectCreate("Server/Listener",
                                 null, // 根据部署描述符来具体定(不一定是 LifecycleListener)
                                 "classNaming");
        digester.addSetProperties("Server/Listener");
        digester.addSetNext("Server/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");




        /**
         * Service 服务实例化
         */
        digester.addObjectCreate("Server/Service",
                                 "org.apache.catalina.core.StandService",
                                 "className");
        digester.addSetProperties("Server/Service");
        digester.addSetNext("Server/Service",
                            "addService",
                            "org.apache.catalina.Service");
        /**
         * Service 监听器实例化
         */
        digester.addObjectCreate("Server/Service/Listener",
                                 null,
                                 "className");
        digester.addSetProperties("Server/Service/Listener");
        digester.addSetNext("Server/Service/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");




        /**
         * Connector 连接器实例化
         */
        digester.addObjectCreate("Server/Service/Connector",
                                 "org.apache.catalina.connector.http10.HttpConnector");
        digester.addSetNext("Server/Service/Connector",
                                 "addConnector",
                                 "org.apache.catalina.Connector");
        /**
         * Connector Factory 工厂实例化
         */
        digester.addObjectCreate("Server/Service/Connector/Factory",
                                 "org.apache.catalina.net.DefaultServerSocketFactory",
                                 "className");
        digester.addSetProperties("Server/Service/Connector/Factory");
        digester.addSetNext("Server/Service/Connector/Factory",
                            "setFactory",
                            "org.apache.catalina.net.ServerSocketFactory");
        /**
         * Connector 监听器实例化
         */
        digester.addObjectCreate("Server/Service/Connector/Listener",
                                 null,
                                 "org.apache.catalina.LifecycleListener");
        digester.addSetProperties("Server/Service/Connector/Listener");
        digester.addSetNext("Server/Service/Connector/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");




        /**
         * Engine 引擎实例化
         */
        digester.addRuleSet(new EngineRuleSet("Server/Service/"));

        /**
         * Engine 下的 Default Context 实例化 与 DefaultContext JNDI 资源实例化
         */
        digester.addRuleSet(new ContextRuleSet("Server/Service/Engine/Default"));
        digester.addRuleSet(new NamingRuleSet("Server/Service/Engine/DefaultContext/"));


        /**
         * Host 主机实例化
         */
        digester.addRuleSet(new HostRuleSet("Server/Service/Engine/"));

        /**
         * Host 下的 Default Context 实例化 与 DefaultContext JNDI 资源实例化
         */
        digester.addRuleSet(new ContextRuleSet("Server/Service/Engine/Host/Default"));
        digester.addRuleSet(new NamingRuleSet("Server/Service/Engine/Host/DefaultContext/"));


        /**
         * Context 应用实例化 与 Context JNDI 资源实例化
         */
        digester.addRuleSet(new ContextRuleSet("Server/Service/Engine/Host/"));
        digester.addRuleSet(new NamingRuleSet("Server/Service/Engine/Host/Context/"));


        /**
         * Engine 父类加载器实例化
         */
        digester.addRule("Server/Service/Engine",
                new SetParentClassLoaderRule(digester,
                        parentClassLoader));

        return digester;
    }

    protected Digester createStopDigetster() {

        Digester digester = new Digester();

        digester.addObjectCreate("Server",
                                "org.apache.core.StandardServer",
                                "className");

        digester.addSetProperties("Server");

        return digester;
    }


    /**
     * Shutdown hook: 提供一种优雅的关闭程序的方式(如果是被kill掉，那么并不会执行该方法)
     */
    protected class CatalineShutdownHook extends Thread{

        public void run() {

            if (server != null) {
                if (server instanceof Lifecycle) {
                    try {
                        ((Lifecycle) server).stop();
                    } catch (LifecycleException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    final class SetParentClassLoaderRule extends Rule {


        ClassLoader parentClassLoader = null;


        public SetParentClassLoaderRule(Digester digester,
                                        ClassLoader parentClassLoader) {

            super(digester);
            this.parentClassLoader = parentClassLoader;

        }

        public void begin(Attributes attributes) throws Exception {

            Container top = (Container) digester.peek();
            top.setParentClassLoader(parentClassLoader);

        }
    }

    // --------------------------------------------------- Properties

    public void setServer(Server server) {
        this.server = server;
    }

    public void setParentClassLoader(ClassLoader parentClassLoader) {
        this.parentClassLoader = parentClassLoader;
    }
}


