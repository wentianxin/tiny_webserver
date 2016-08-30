package org.apache.tomcat.util.net;

import java.net.InetAddress;

/**
 * Created by tisong on 8/21/16.
 */
public abstract class AbstractEndpoint {

    /**
     * Controls when the Endpoint binds the port. <code>true</code>, the default
     * binds the port on {@link #init()} and unbinds it on {@link #destroy()}.
     * If set to <code>false</code> the port is bound on {@link #start()} and
     * unbound on {@link #stop()}.
     * 控制Endpoint在哪个方法中绑定端口
     * true: init() - destory()
     * false: start() - stop()
     */
    private boolean bindOnInit = true;

    private BindState bindState = BindState.UNBOUND;


    private int port;

    private int backlog = 100;

    private InetAddress address;

    /**
     * Threads used to accept new connections and pass them to worker threads.
     */
    protected Acceptor[] acceptors;

    /**
     * Acceptor thread count
     */
    protected int acceptorThreadCount = 0;



    // TODO volatile关键字的作用
    protected volatile boolean running = false;


    protected volatile boolean paused = false;



    private int maxConnections = 10000;




    public abstract void bind() throws Exception;

    public abstract void unbind() throws Exception;

    public abstract void startInternal() throws Exception;

    public abstract void stopInternal() throws Exception;


    public final void init() throws Exception {
        if (bindOnInit) {
            bind();
            bindState = BindState.BOUND_ON_INIT;
        }
    }

    public final void start() throws Exception {

        startInternal();
    }

    protected final void startAcceptorThreads() {
        int count = acceptorThreadCount;

        acceptors = new Acceptor[count];

        for (int i = 0; i < count; i++) {
            acceptors[i] = createAcceptor();
            String threadName = getName() + "-Acceptor-" + i;
            acceptors[i].setThreadName(threadName);
            Thread t = new Thread(acceptors[i], threadName);
            t.setPriority(getAcceptorThreadPriority());
            t.setDaemon(getDaemon());
            t.start();
        }
    }

    public static interface Handler {
        /**
         * Different types of socket states to react upon. 做出的反应
         */
        public enum SocketState {
            OPEN, CLOSED, LONG, ASYNC_END, SENDFILE, UPGRADING, UPGRADED
        }
    }

    /**
     * 端口绑定状态
     */
    protected enum BindState {
        UNBOUND, BOUND_ON_INIT, BOUND_ON_START
    }

    public abstract static class Acceptor implements Runnable {

        public enum AcceptorState {
            NEW, RUNNING, PAUSED, ENDED
        }

        protected volatile AcceptorState state = AcceptorState.NEW;

        public final AcceptorState getState() {
            return state;
        }

        private String threadName;

        public void setThreadName(String threadName) {
            this.threadName = threadName;
        }

        public String getThreadName() {
            return threadName;
        }
    }
}
