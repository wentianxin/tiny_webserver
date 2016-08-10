package org.apache.catalina.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Created by tisong on 8/10/16.
 */
public interface ServerSocketFactory {

    public ServerSocket createSocket(int port) throws IOException;

    public ServerSocket createSocket(int port, int backlog) throws IOException;

    public ServerSocket createSocket(int port, int backlog, InetAddress ifAddress) throws IOException;

}
