package org.apache.catalina.connector.http;


import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.util.LifecycleSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by tisong on 8/10/16.
 */
public class HttpProcessor implements Lifecycle, Runnable{

    private int id;

    private HttpConnector httpConnector;

    private LifecycleSupport lifecycle;

    private boolean started;

    private Thread thread;

    private String threadName;

    public HttpProcessor(HttpConnector httpConnector, int id) {
        this.httpConnector = httpConnector;
        this.id = id;
    }


    public synchronized void assign(Socket socket) {

    }

    public synchronized Socket await() {

    }

    @Override
    public void run() {

        while(!stopped) {

            Socket socket = await();

            process(socket);
        }
    }

    // ------------------------------------------------------------- Implements Lifecycle
    @Override
    public void start() throws LifecycleException {
        if (started) {

        }

        this.started = true;

        lifecycle.fireLifecycleEvent(START_EVENT, null);

        threadStart();
    }

    @Override
    public void stop() throws LifecycleException {

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

    // ----------------------------------------------------------- Private Methods

    private void threadStart() {

        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();
    }

    private void threadStop() {

    }


    private void process(Socket socket) {

        SocketInputStream inputStream = null;
        OutputStream outputStream = null;

        inputStream = new SocketInputStream(socket.getInputStream());
        outputStream = socket.getOutputStream();

        while(true) {

            request.setStream(inputStream);
            request.setResponse(response);
            response.setStream(outputStream);

            ((HttpServletRequest)response.getResponse()).setHeader("Server", SERVER_INFO);

            parseConnection(socket);
            parseRequest(input, output);
            if (request.getRequest().getProtocol().startWith("HTTP/0")) {
                parseHeaders(input);
            }
            if (http11) {
                ackRequest(output);
            }


            ((HttpServletResponse)response.getReponse()).setHeader("Date", FastHttpDateFormat.getCurrentDate());

            conenctor.getContainer().invoke(request, response);

            if (finishResponse) {
                response.finishResponse();
            }

            request.recycle();
            response.recycle();
        }


        try {
            shutdownInput(inputStream);
            socket.close();
        } catch (IOException e) {

        }


    }
}
