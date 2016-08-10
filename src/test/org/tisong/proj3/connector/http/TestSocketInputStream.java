package org.tisong.proj3.connector.http;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Created by tisong on 8/9/16.
 */
public class TestSocketInputStream {
    @Test
    public void readHeader() throws Exception {

    }

    @Test
    public void readRequestLine() throws Exception {
        SocketInputStream socketInputStream = getSocketInputStream();
        HttpRequestLine requestLine = new HttpRequestLine();
        socketInputStream.readRequestLine(requestLine);

        System.out.println(requestLine.method + " " + requestLine.uri + " " + requestLine.protocol);

    }

    private SocketInputStream getSocketInputStream() {
        String requestLine = "GET / HTTP \n";
        InputStream stream = new ByteArrayInputStream(requestLine.getBytes(StandardCharsets.UTF_8));

        return new SocketInputStream(stream, 2048);
    }
}