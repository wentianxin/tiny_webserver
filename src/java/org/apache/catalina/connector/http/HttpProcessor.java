package org.apache.catalina.connector.http;


import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.util.LifecycleSupport;

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

    public synchronized void await() {

    }

    @Override
    public void run() {

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


}
