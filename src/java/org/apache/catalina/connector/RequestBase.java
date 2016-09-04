package org.apache.catalina.connector;

import org.apache.catalina.Connector;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.util.StringManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.*;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * 实现了 ServletRequest 与 Request 接口
 */
public abstract class RequestBase
    implements ServletRequest, Request {

    protected static StringManager sm =
            StringManager.getManager(Constants.Package);

    protected String authorization = null;

    protected String characterEncoding = null;

    protected int contentLength;

    protected String contentType;

    protected String protocol = null;

    protected String scheme = null;

    protected String serverName = null;

    protected int serverPort ;


    protected RequestFacade facade = new RequestFacade(this);

    protected Response response = null;

    protected Socket socket = null;

    protected InputStream input = null;

    protected BufferedReader reader = null;

    protected ServletInputStream stream = null;



    protected Connector connector = null;


    // ------------------------------------------------ Abstract ServletRequest

    @Override
    public abstract String getParameter(String name);

    @Override
    public abstract Enumeration getParameterNames();

    @Override
    public abstract String[] getParameterValues(String name);

    @Override
    public abstract Map getParameterMap();

    @Override
    public abstract RequestDispatcher getRequestDispatcher(String path);



    @Override
    public Object getAttribute(String s) {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {
        this.characterEncoding = s;
    }

    @Override
    public int getContentLength() {
        return this.contentLength;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (reader != null) {
            throw new IllegalStateException
                    (sm.getString("requestBase.getInputStream.ise"));
        }

        if (stream == null) {
            stream = createInputStream();
        }

        return stream;
    }




    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public String getServerName() {
        return this.serverName;
    }

    @Override
    public int getServerPort() {
        return this.serverPort;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (stream != null) {
            throw new IllegalStateException
                    (sm.getString("requestBase.getReader.ise"));
        }

        if (reader == null) {
            String encoding = getCharacterEncoding();
            if (encoding == null) {
                encoding = "ISO-8859-1";
            }
            InputStreamReader isr =
                    new InputStreamReader(createInputStream(), encoding);
            reader = new BufferedReader(isr);
        }
        return reader;
    }

    @Override
    public String getRemoteAddr() {
        return null;
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }



    @Override
    public String getRealPath(String s) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }




    // ------------------------------------------ Implements Request

    @Override
    public String getAuthorization() {
        return this.authorization;
    }

    @Override
    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    @Override
    public Connector getConnector() {
        return this.connector;
    }

    @Override
    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    @Override
    public ServletRequest getRequest() {
        return this.facade;
    }

    @Override
    public Response getResponse() {
        return this.response;
    }

    @Override
    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public InputStream getStream() {
        return this.input;
    }


    @Override
    public void setStream(InputStream stream) {
        this.input = stream;
    }

    @Override
    public ServletInputStream createInputStream() throws IOException {
        return new RequestStream(this);
    }

    @Override
    public void finishRequest() throws IOException {

        if (reader != null) {
            reader.close();
        }

        if (stream != null) {
            stream.close();
        }
    }

    @Override
    public void recycle() {
        //attributes.clear();
        authorization = null;
        characterEncoding = null;
        // connector is NOT reset when recycling
        contentLength = -1;
        contentType = null;
        //context = null;
        input = null;
        //locales.clear();
        //notes.clear();
        protocol = null;
        reader = null;
        //remoteAddr = null;
        //remoteHost = null;
        response = null;
        scheme = null;
        //secure = false;
        serverName = null;
        serverPort = -1;
        socket = null;
        stream = null;
        //wrapper = null;
    }



    @Override
    public void setContentLength(int length) {
        this.contentLength = length;
    }

    @Override
    public void setContentType(String type) {
        this.contentType = type;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public void setServerName(String name) {
        this.serverName = name;
    }

    @Override
    public void setServerPort(int port) {
        this.serverPort = port;
    }
}
