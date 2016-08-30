package org.apache.tomcat.util.net;

import org.apache.catalina.net.DefaultServerSocketFactory;
import org.apache.catalina.net.ServerSocketFactory;
import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tisong on 8/21/16.
 */
public class JIoEndpoint extends AbstractEndpoint {

    private ServerSocket serverSocket = null;

    private ServerSocketFactory serverSocketFactory = null;


    private Handler handler = null;








    public int getPort() {
        return this.port;
    }

    public int getBacklog() {
        return this.backlog;
    }

    public InetAddress getAddress() {
        return this.address;
    }


    @Override
    public void bind() throws Exception {
        if (acceptorThreadCount == 0) {
            acceptorThreadCount = 1;
        }



        if (serverSocketFactory == null) {
            if (isSSLEnabled()) {

            } else {
                serverSocketFactory = new DefaultServerSocketFactory();
            }
        }

        if (serverSocket == null) {

            try {
                if (getAddress() == null) {
                    serverSocket = serverSocketFactory.createSocket(getPort(), getBacklog());
                } else {
                    serverSocket = serverSocketFactory.createSocket(getPort(), getBacklog(), getAddress());
                }
            } catch (BindException e) {

            }
        }
    }

    @Override
    public void startInternal() throws Exception {


        if (!running) {
            running = true;
            paused =false;

            // Create worker collection
            if (getExecutor() == null) {
                createExecutor();
            }


            initializeConnectionLatch();

            startAcceptorThreads();

            Thread timeoutThread = new Thread(new AsyncTimeout(),
                    getName() + "-AsyncTimeout");
            timeoutThread.setPriority(threadPriority);
            timeoutThread.setDaemon(true);
            timeoutThread.start();
        }
    }


    @Override
    public AbstractEndpoint.Acceptor createAcceptor() {
        return new Acceptror();
    }

    public interface Handler extends AbstractEndpoint.Handler {
        public SocketState process(SocketWrapper<Socket> socket,
                                   SocketStatus status);
    }

    protected class Acceptror extends AbstractEndpoint.Acceptor {

        @Override
        public void run() {

            while (running) {


                state = AcceptorState.RUNNING;

                try {
                    Socket socket = null;

                    try {
                        socket = serverSocketFactory.acceptSocket(serverSocket);
                    } catch (IOException e) {

                    }

                    if (running && !paused && setSocketOptions(socket)) {

                        if (!processSocket(socket)) {

                        }
                    }

                } catch (IOException x) {

                }
            }
        }
    }

    protected class SocketProcessor implements Runnable {

        protected SocketWrapper<Socket> socket = null;

        protected SocketStatus status = null;

        public SocketProcessor(SocketWrapper<Socket> socket) {
            this.socket = socket;
        }

        public SocketProcessor(SocketWrapper<Socket> socket, SocketStatus status) {
            this.socket = socket;
            this.status = status;
        }

        @Override
        public void run() {

            SocketState state = SocketState.OPEN;

            if (state != SocketState.CLOSED) {
                if (state == null) {
                    state = handler.process(socket, SocketStatus.OPEN);
                } else {
                    state = handler.process(socket, status);
                }
            }

            if (state == SocketState.CLOSED) {

                // Close socket
            } else if (state == SocketState.OPEN) {

            }

            socket = null;
        }
    }

    protected class AsyncTimeout implements Runnable {

        @Override
        public void run() {

            // Loop until we receive a shutdown command
            while (running) {



            }
        }
    }

    protected boolean processSocket(Socket socket) {


        SocketWrapper<Socket> wrapper = new SocketWrapper<Socket>(socket);

        getExecutor().execute(new SocketProcessor(wrapper));
    }
}
