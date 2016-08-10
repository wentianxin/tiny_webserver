package org.apache.catalina.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Created by tisong on 8/10/16.
 */
public final class DefaultServerSocketFactory implements ServerSocketFactory{

    // TODO 关于抛出的异常问题

    @Override
    public ServerSocket createSocket(int port) throws IOException {

        return new ServerSocket(port);
    }

    @Override
    public ServerSocket createSocket(int port, int backlog) throws IOException {

        return new ServerSocket(port, backlog);
    }

    @Override
    public ServerSocket createSocket(int port, int backlog, InetAddress ifAddress) throws IOException {

        return new ServerSocket(port, backlog, ifAddress);
    }
}
