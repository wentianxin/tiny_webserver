package org.tisong.proj3.connector.http;

import org.apache.catalina.util.StringManager;
import org.tisong.proj3.ServletProcessor;
import org.tisong.proj3.StaticResourceProcessor;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by tisong on 8/7/16.
 */
public class HttpProcessor {

    private HttpRequestLine requestLine;
    private HttpRequest     request;
    private HttpResponse    response;
    private HttpConnector   httpConnector;

    private final StringManager   sm = StringManager.getManager(Constants.Package);

    public HttpProcessor(HttpConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    public void process(Socket socket) {

        try {
            SocketInputStream input = new SocketInputStream(socket.getInputStream(), 2048);
            OutputStream output = socket.getOutputStream();

            parseRequest(input, output);
            parseHeaders(input);

            /* 根据解析的请求来判断请求的是静态资源文件还是servlet */

            if (request.getRequestURI().startsWith("/servlet")) {
                ServletProcessor processor = new ServletProcessor();
                processor.process(request, response);
            } else {
                StaticResourceProcessor processor = new StaticResourceProcessor();
                processor.process(request, response);
            }

            socket.close();
        } catch (IOException e) {

        } catch (ServletException e) {

        }
    }

    /**
     * http的头部解析 (only parses easy headers)
     * such as "cookie", "content-length", "content-type"
     * @param input
     */
    private void parseHeaders(SocketInputStream input)
            throws IOException, ServletException {
        HttpHeader header = null;

        while (true) {
            header = new HttpHeader();

            input.readHeader(header);
            if (header.nameEnd == 0) {
                if (header.valueEnd == 0) {
                    return ;
                }
                else {
                    throw new ServletException((sm.getString("httpProcessor.parseHeaders.colon")));
                }
            }

            String name  = new String(header.name,  0, header.nameEnd );
            String value = new String(header.value, 0, header.valueEnd);

            request.addHeader(name, value);

            if (name.equals("cookie")) {

            } else if (name.equals("content-length")) {

            }
        }
    }

    private void parseRequest(SocketInputStream input, OutputStream output)
            throws IOException{
        requestLine = new HttpRequestLine();

        input.readRequestLine(requestLine);

        String method   = new String(requestLine.method,   0, requestLine.methodEnd);
        String uri      = new String(requestLine.uri,      0, requestLine.uriEnd);
        String protocol = new String(requestLine.protocol, 0, requestLine.protocolEnd);

        /* 检测 method uri protocol 长度是否正确 */
        if (method.length() < 1) {

        } else if (uri.length() < 1) {

        } else if (protocol.length() < 1) {

        }

        /* 检测 uri 是否含有请求参数 */
        int question = uri.indexOf("?");
        if (question != -1) {
            uri = new String(requestLine.uri, 0, question);
            request.setQueryString(new String(requestLine.uri, question+1, requestLine.uriEnd-question-1));
        }

        // TODO
        if (!uri.startsWith("/")) {
            int pos = uri.indexOf("://");
            if (pos != -1) {

            }
        }
        /* 检测是否含有 sessionid  */

        /* normalized 检查 */
    }

    private String normalize(String path) {
        return "";
    }
}
