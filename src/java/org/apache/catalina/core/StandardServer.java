package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.deploy.NamingResources;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.naming.*;
import javax.naming.Context;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tisong on 9/6/16.
 */
public class StandardServer
    implements Server, Lifecycle{

    private static final Log logger = LogFactory.getLog(StandardServer.class);

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);

    private int port = 8005;

    private String shutdown = "SHUTDOWN";

    private Service[] services = new Service[0];


    private boolean initialized = false;

    private boolean started = false;


    /**
     * 全局资源
     */
    private NamingResources globalNamingResources = null;

    private javax.naming.Context globalNamingContext = null;



    private NamingContextListener namingContextListener = null;

    private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);



    public StandardServer() {

        logger.info("StandServer 实例化");

        globalNamingResources = new NamingResources();
        globalNamingResources.setContainer(this);

        if (isUseNaming() && namingContextListener == null) {
            namingContextListener = new NamingContextListener();
            addLifecycleListener(namingContextListener);
        }
    }


    // ------------------------------------------------- Implements Server Method(not property)


    /**
     * 初始化 services 服务
     */
    @Override
    public void initialize() throws LifecycleException {

        if (initialized) {
            throw new LifecycleException();
        }
        initialized = true;

        for (Service service: services) {
            service.initialize();
        }
    }

    @Override
    public void await() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Socket socket = null;



        try {
            socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String line = reader.readLine();
                if (line.equals(shutdown)) {
                    break;
                } else {
                    System.err.println("StandardServer.await: Invalid command '" +
                            line.toString() + "' received");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null && socket != null ) {
                try {
                    socket.close();
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }



    }


    // ------------------------------------- Implements Lifecycle

    @Override
    public void start() throws LifecycleException {

        if (started) {
            throw new LifecycleException();
        }


        lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);

        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

        started = true;

        for (Service service: services) {
            if (service instanceof Lifecycle) {
                ((Lifecycle) service).start();
            }
        }

        lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    @Override
    public void stop() throws LifecycleException {

        if (!started) {
            throw new LifecycleException
                    (sm.getString("standardServer.stop.notStarted"));
        }
        lifecycleSupport.fireLifecycleEvent(BEFORE_STOP_EVENT, null);
        lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

        started = false;

        for (Service service: services) {
            if (service instanceof Lifecycle) {
                ((Lifecycle) service).stop();
            }
        }

        lifecycleSupport.fireLifecycleEvent(AFTER_STOP_EVENT, null);
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.addLifecycleListener(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.removeLifecycleListener(listener);
    }








    // ------------------------------------- Private Methods

    /**
     * 根据系统变量(catalina.useNaming)来判断是否使用 JNDI 资源
     */
    private boolean isUseNaming() {
        boolean useNaming = true;
        // Reading the "catalina.useNaming" environment variable
        String useNamingProperty = System.getProperty("catalina.useNaming");
        if ((useNamingProperty != null)
                && (useNamingProperty.equals("false"))) {
            useNaming = false;
        }
        return useNaming;
    }



    // ------------------------------------- Implements Server Properties

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String getShutdown() {
        return this.shutdown;
    }

    @Override
    public void setShutdown(String shutdown) {
        this.shutdown = shutdown;
    }


    @Override
    public void addService(Service service) {

        Service[] results = new Service[services.length + 1];
        System.arraycopy(services, 0, results, 0, services.length);

        results[services.length] = service;

        services = results;
    }

    @Override
    public Service[] findServices() {
        return this.services;
    }

    @Override
    public void removeService(Service service) {

        int pos = 0;
        for (; pos < services.length; pos++) {
            if (services[pos] == service) {
                break;
            }
        }

        if (pos == services.length) {
            return ;
        }

        if (services[pos] instanceof Lifecycle) {
            try {
                ((Lifecycle)services[pos]).stop();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }

        Service[] results = new Service[services.length - 1];
        for (int i = 0, k = 0; i < services.length; i++) {
            if (i == pos) {
                continue;
            }
            results[k++] = services[i];
        }

        services = results;
    }


    /**
     * JNDI 资源管理与绑定
     */

    public void setGlobalNamingResources(NamingResources globalNamingResources) {
        this.globalNamingResources = globalNamingResources;
    }
    public NamingResources getGlobalNamingResources() {
        return globalNamingResources;
    }

    public void setNamingContextListener(NamingContextListener namingContextListener) {
        this.namingContextListener = namingContextListener;
    }
    public NamingContextListener getNamingContextListener() {
        return namingContextListener;
    }

    public void setGlobalNamingContext(javax.naming.Context globalNamingContext) {
        this.globalNamingContext = globalNamingContext;
    }
    public Context getGlobalNamingContext() {
        return globalNamingContext;
    }
}
