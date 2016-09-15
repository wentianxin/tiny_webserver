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
 * 连接器: 创建ServerSocket 接收Socket, 分发给HttpProcessor线程处理
 * Created by tisong on 9/2/16.
 */
public class HttpConnector implements Connector, Lifecycle, Runnable{

    private StringManager sm = StringManager.getManager(Constants.Package);

    /**
     * 服务器绑定的信息
     */
    private int port = 8080;        // 端口号

    private String scheme = "http"; // 协议

    private String address = null;  // 地址; null: 绑定所有地址;

    /**
     * 连接器接收的 tcp 最大连接数
     */
    private int acceptCount;


    private ServerSocketFactory factory = null;

    private ServerSocket serverSocket = null;

    /**
     * Socket输入流接收的缓冲区大小
     */
    private int bufferSize = 2048;


    private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);


    private boolean initialized = false;

    private boolean started = false;

    private boolean stopped = false;


    /**
     * 后台线程: 接收 socket, 并分发给 <code>HttpProcessor</code>
     */
    private Thread backgroundThread = null;

    private String threadName = null;

    private Object threadSyc = new Object();


    /**
     * HttpProcessor 缓冲池
     */
    private Stack<HttpProcessor> processors = new Stack<HttpProcessor>();

    private List<HttpProcessor> createdProcessors = new ArrayList<HttpProcessor>();

    private int minProcessors = 10;  // 启动时创建HttpProcessor数量

    private int maxProcessors = 50;  // 最大HttpProcessor数量

    private int curProcessors = 0;   // 当前已创建HttpProcessor数量



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
        this.factory = factory;
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
            throw new LifecycleException(threadName + ".open", e);
        }
    }


    @Override
    public Request createRequest() {

        HttpRequestImpl request = new HttpRequestImpl();
        request.setConnector(this);
        return request;
    }

    @Override
    public Response createResponse() {
        HttpResponseImpl response = new HttpResponseImpl();
        response.setConnector(this);
        return response;
    }





    // ---------------------------------------  impelement Lifecycle

    @Override
    public void start() throws LifecycleException {

        if (started) {
            throw new LifecycleException(sm.getString("httpConnector.alreadyStarted"));
        }

        threadName = "HttpConnector[" + port + "]";

        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

        started = true;

        threadStart();

        while (curProcessors < minProcessors) {
            if ((maxProcessors > 0) && (curProcessors >= maxProcessors))
                break;
            createProcessor();
        }
    }

    @Override
    public void stop() throws LifecycleException {
        if (!started) {

        }

        lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

        started = false;

        for (HttpProcessor processor: createdProcessors) {

            if (processor instanceof Lifecycle) {
                try {
                    ((Lifecycle) processor).stop();
                } catch (LifecycleException e) {

                }
            }

            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {

                }
                serverSocket = null;
            }
        }

        threadStop();
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.addLifecycleListener(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.removeLifecycleListener(listener);
    }


    // ----------------------------------- impelements Runnable

    @Override
    public void run() {

        while (!stopped) {

            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {

            }

            HttpProcessor httpProcessor = alloateProcessor();
            if (httpProcessor == null) {
                try {
                    socket.close();
                } catch (IOException e) {

                }
                continue;
            }

            httpProcessor.assign(socket);
        }

        synchronized (threadSyc) {
            threadSyc.notifyAll();
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

        synchronized (processors) {
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

    }

    private HttpProcessor createProcessor() {

        HttpProcessor processor = new HttpProcessor(this, curProcessors);

        if (processor instanceof Lifecycle) {
            try {
                ((Lifecycle) processor).start();
            } catch (LifecycleException e) {

                return null;
            }
        }
        createdProcessors.add(processor);

        processors.push(processor);

        return processor;
    }


    public void recycleProcessor(HttpProcessor processor) {
        processors.push(processor);
    }


    private void threadStart() {

        backgroundThread = new Thread(this, threadName);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }

    private void threadStop() {

        stopped = true;

        synchronized (threadSyc) {
            try {
                threadSyc.wait(5000);
            } catch (InterruptedException e) {

            }
        }

        backgroundThread = null;
    }
    // -----------------------------------


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }



    public int getBufferSize(){
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
