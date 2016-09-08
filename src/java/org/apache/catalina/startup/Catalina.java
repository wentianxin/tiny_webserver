package org.apache.catalina.startup;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.tomcat.util.digester.Digester;
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


    protected String configFile = "conf/server.xml";


    protected boolean starting = false;

    protected boolean stopping = false;

    protected boolean useNaming = true;


    protected Server server = null;



    protected File configFile() {
        File file = new File(configFile);

        return file;
    }

    protected void setCatalinaHome() {

        if (System.getProperty("catalina.home") != null) {
            return;
        }
        System.setProperty("catalina.home",
                System.getProperty("user.dir"));

    }

    protected void setCatalinaBase() {

        if (System.getProperty("catalina.base") != null) {
            return;
        }
        System.setProperty("catalina.base",
                System.getProperty("catalina.home"));

    }

    protected boolean arguments(String[] args) {

        boolean isConfig = false;

        return true;
    }

    protected void execute() throws Exception {

        start();
    }

    public void process(String[] args) {

        /**
         * 设置系统变量: catalina.home usr.dir(即为用户的当前目录)
         */
        setCatalinaHome();
        setCatalinaBase();

        try {
            execute();
        } catch (Exception e) {
            e.printStackTrace();
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
     *
     * @return
     */
    protected Digester createStartDigester() {

        Digester digester = new Digester();

        digester.setValidating(false);

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
}
