package org.tisong.proj3.connector.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by tisong on 8/7/16.
 */
public class HttpConnector implements Runnable{

    private boolean stopped;

    private String scheme = "http";

    private final int port = 8080;

    private final String host = "127.0.0.1";

    @Override
    public void run() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName(host));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while(!stopped) {

            Socket socket = null;

            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }

            HttpProcessor httpProcessor = new HttpProcessor(this);
            httpProcessor.process(socket);
        }

    }

    public void start() {
        Thread thread = new Thread(this);

        thread.start();
    }
}
