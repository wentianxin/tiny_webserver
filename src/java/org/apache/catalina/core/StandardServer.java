package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

/**
 * Created by tisong on 9/6/16.
 */
public class StandardServer
    implements Server, Lifecycle{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);

    private int port = 8005;

    private String shutdown = "shuddown";

    private Service[] services = new Service[0];


    private boolean initialized = false;

    private boolean started = false;


    private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);


    // ------------------------------------------------- Implements Server

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
    public void await() {

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
     * 初始化 services 服务
     * @throws LifecycleException
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



    // ------------------------------------- Implements Lifecycle

    @Override
    public void start() throws LifecycleException {

        if (started) {
            throw new LifecycleException();
        }
        started = true;


        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
        for (Service service: services) {
            if (service instanceof Lifecycle) {
                ((Lifecycle) service).start();
            }
        }
    }

    @Override
    public void stop() throws LifecycleException {

        if (!started) {
            throw new LifecycleException
                    (sm.getString("standardServer.stop.notStarted"));
        }

        lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

        started = false;

        for (Service service: services) {
            if (service instanceof Lifecycle) {
                ((Lifecycle) service).stop();
            }
        }

    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.addLifecycleListener(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.removeLifecycleListener(listener);
    }
}
