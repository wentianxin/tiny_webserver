package org.apache.tomcat.util.net;

import org.apache.catalina.net.DefaultServerSocketFactory;
import org.apache.catalina.net.ServerSocketFactory;

import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Created by tisong on 8/21/16.
 */
public class JIoEndpoint extends AbstractEndpoint {

    private ServerSocket serverSocket = null;

    private ServerSocketFactory serverSocketFactory = null;

    private int port;

    private int backlog = 100;

    private InetAddress address;

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
}
