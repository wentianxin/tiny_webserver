package org.apache.catalina.connector.http10;

import org.apache.catalina.*;
import org.apache.catalina.connector.http.*;
import org.apache.catalina.net.DefaultServerSocketFactory;
import org.apache.catalina.net.ServerSocketFactory;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by tisong on 9/2/16.
 */
public class HttpConnector implements Connector, Lifecycle, Runnable{

    private StringManager sm = StringManager.getManager(Constants.Package);

    private int port = 8080;

    private String scheme = "http";

    private String address = null;

    /**
     * 连接器接收的 tcp 最大连接数
     */
    private int acceptCount;



    private ServerSocketFactory factory = null;

    private ServerSocket serverSocket = null;


    private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);


    private boolean initialized = false;

    private boolean started = false;

    private boolean running = false;

    private boolean stopped = false;

    /**
     * 后台线程: 接收 socket, 并分发给 <code>HttpProcessor</code>
     */
    private Thread backgroundThread = null;

    private String threadName = null;

    /**
     * HttpProcessor 缓冲池
     */
    private Stack<HttpProcessor> processors = new Stack<HttpProcessor>();

    private List<HttpProcessor> createdProcessors = new ArrayList<HttpProcessor>();

    private int minProcessors = 10;

    private int maxProcessors = 50;

    private int curProcessors = 0;



    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {

    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public String getScheme() {
        return scheme;
    }

    @Override
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public ServerSocketFactory getFactory() {

        if (this.factory == null) {
            // TODO 关于线程安全问题
            synchronized (this) {
                this.factory = new DefaultServerSocketFactory();
            }
        }

        return this.factory;
    }

    @Override
    public void setFactory(ServerSocketFactory factory) {

    }

    @Override
    public void initialize() throws LifecycleException {

        if (initialized) {
            throw new LifecycleException(sm.getString("httpConnector.alreadyInitiaalized"));
        }

        this.initialized = true;

        try {
            serverSocket = openServerSocket();
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





    // ---------------------------------------  impelement Lifecycle

    @Override
    public void start() throws LifecycleException {

        if (started) {
            throw new LifecycleException(sm.getString("httpConnector.alreadyStarted"));
        }

        threadName = "HttpConnector[" + port + "]";

        backgroundThread = new Thread(this, threadName);
        backgroundThread.setDaemon(true);
        backgroundThread.start();

        for (; curProcessors < minProcessors; curProcessors++) {


        }
    }

    @Override
    public void stop() throws LifecycleException {

        for (HttpProcessor processor: createdProcessors) {

            try {
                ((Lifecycle) processor).stop();
            } catch (LifecycleException e) {

            }

            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {

                }
                serverSocket = null;
            }

        }


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


    // ----------------------------------- impelements Runnable

    @Override
    public void run() {

        while (!stopped) {

            Socket socket = null;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            HttpProcessor httpProcessor = alloateProcessor();
            if (httpProcessor == null) {

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            httpProcessor.assign(socket);
        }
    }

    // -----------------------------------  private method

    /**
     * 关于端口绑定的知识点(port, address, backlog) 以及 InetAddress的使用
     * @return
     * @throws IOException
     */
    private ServerSocket openServerSocket() throws IOException {

        ServerSocketFactory factory = getFactory();

        if (address == null) {

            return factory.createSocket(port, acceptCount);
        }

        InetAddress[] addresses =
                InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
        int i;
        for (i = 0; i < addresses.length; i++) {
            if (addresses[i].getHostAddress().equals(address))
                break;
        }
        if (i < addresses.length) {
            return factory.createSocket(port, acceptCount, addresses[i]);
        } else {
            return factory.createSocket(port, acceptCount);
        }
    }


    private HttpProcessor alloateProcessor() {

        if (processors.size() > 0) {
            return processors.pop();
        } else {

            if (curProcessors < maxProcessors) {
                return createProcessor();
            } else {
                return null;
            }
        }

    }

    private HttpProcessor createProcessor() {

        HttpProcessor processor = new HttpProcessor(this, curProcessors);

        if (processor instanceof Lifecycle) {
            try {
                ((Lifecycle) processor).start();
            } catch (LifecycleException e) {

            }
        }
        createdProcessors.add(processor);

        processors.push(processor);

        return processor;
    }



    private void threadStop() {

        stopped = true;

    }
    // -----------------------------------


}
