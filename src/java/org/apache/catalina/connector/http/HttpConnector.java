package org.apache.catalina.connector.http;

import org.apache.catalina.*;
import org.apache.catalina.net.DefaultServerSocketFactory;
import org.apache.catalina.net.ServerSocketFactory;
import org.apache.catalina.util.LifecycleSupport;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Stack;

/**
 * Created by tisong on 8/9/16.
 */
public class HttpConnector implements Connector, Lifecycle, Runnable{

    private String address;

    private int port;

    private int acceptCount;

    private Container contaienr;

    private ServerSocket serverSocket;

    private ServerSocketFactory factory;

    private LifecycleSupport lifecycle;

    private List createdProcessors;

    private Stack processors;

    private Thread thread;

    private String threadName;

    private int curProcessors;

    private int minProcessors;

    private int maxProcessors;


    private boolean initialized;

    private boolean started;



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

        if (started) {
            throw new LifecycleException();
        }

        lifecycle.fireLifecycleEvent(START_EVENT, null);

        this.started = true;

        threadStart();

        while(curProcessors < minProcessors) {
            HttpProcessor processor = newProcessor();
            recycleProcessor(processor);
        }
    }

    @Override
    public void stop() throws LifecycleException {

    }

    @Override
    public void run() {

        Socket socket = null;

        try {
            socket = serverSocket.accept();

        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpProcessor processor = alloateProcessor();
        if (processor == null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        processor.assign(socket);
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




    // -------------------------------------------------------- Private methods

    private ServerSocket open() throws IOException {
        ServerSocketFactory factory = getFactory();

        if (address == null) {
            return factory.createSocket(port, acceptCount);
        }


        InetAddress is = InetAddress.getByName(address);

        return factory.createSocket(port, acceptCount, is);

    }

    private void threadStart() {

        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    private HttpProcessor newProcessor() {
        HttpProcessor processor = new HttpProcessor(this, curProcessors++);

        if (processor instanceof Lifecycle) {
            try {
                ((Lifecycle) processor).start();
            } catch (LifecycleException e) {

                return null;
            }
        }

        createdProcessors.add(processor);
        return processor;
    }

    private HttpProcessor alloateProcessor() {
        synchronized (processors) {
            if (processors.size() > 0) {
                return (HttpProcessor) processors.pop();
            } else if (curProcessors >= maxProcessors) {
                return null;
            } else {
                return newProcessor();
            }
        }
    }

    private void recycleProcessor(HttpProcessor processor) {

        processors.push(processor);
    }



    // ------------------------------------------------------- implements Connector Properties

    @Override
    public Container getContainer() {
        return contaienr;
    }

    @Override
    public void setContainer(Container container) {
        this.contaienr = container;
    }

    @Override
    public ServerSocketFactory getFactory() {

        if (this.factory == null) {
            this.factory = new DefaultServerSocketFactory();
        }

        return this.factory;
    }

    @Override
    public void setFactory(ServerSocketFactory factory) {

    }
}
