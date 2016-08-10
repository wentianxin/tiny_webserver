package org.apache.catalina.connector.http;

import org.apache.catalina.*;

import java.net.ServerSocket;

/**
 * Created by tisong on 8/9/16.
 */
public class HttpConnector implements Connector, Lifecycle, Runnable{

    private ServerSocket serverSocket;

    // ------------------------------------------------------- implements Connector

    /**
     *
     * @throws LifecycleException
     */
    @Override
    public void initialize() throws LifecycleException {
        
    }

    @Override
    public Request createRequest() {
        return null;
    }

    @Override
    public Response createResponse() {
        return null;
    }


    // ------------------------------------------------------ implements Lifecycle, Runnable
    @Override
    public void start() throws LifecycleException {

    }

    @Override
    public void stop() throws LifecycleException {

    }

    @Override
    public void run() {

    }


    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListener() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }


}
