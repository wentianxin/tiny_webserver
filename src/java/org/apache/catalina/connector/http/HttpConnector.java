package org.apache.catalina.connector.http;

import org.apache.catalina.*;
import org.apache.catalina.net.DefaultServerSocketFactory;
import org.apache.catalina.net.ServerSocketFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Created by tisong on 8/9/16.
 */
public class HttpConnector implements Connector, Lifecycle, Runnable{

    private String address;

    private int port;

    private int acceptCount;

    private ServerSocket serverSocket;

    private ServerSocketFactory factory;



    // ------------------------------------------------------- implements Connector

    /**
     *
     * @throws LifecycleException
     */
    @Override
    public void initialize() throws LifecycleException {

        try {
            serverSocket = open();
        } catch (IOException e) {
            
        }
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


    private ServerSocket open() throws IOException {
        ServerSocketFactory factory = getFactory();

        if (address == null) {
            return factory.createSocket(port, acceptCount);
        }


        InetAddress is = InetAddress.getByName(address);

        return factory.createSocket(port, acceptCount, is);

    }

    private ServerSocketFactory getFactory() {

        if (this.factory == null) {
            this.factory = new DefaultServerSocketFactory();
        }

        return this.factory;
    }
}
