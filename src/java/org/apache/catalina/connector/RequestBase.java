package org.apache.catalina.connector;

import org.apache.catalina.Request;
import org.apache.catalina.Response;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * Created by tisong on 9/3/16.
 */
public abstract class RequestBase implements Request, ServletRequest{


    protected InputStream input = null;

    private ServletInputStream stream = null;

    private BufferedReader reader = null;


    protected RequestFacade facade = new RequestFacade(this);





    private int contentLength = -1;

    protected String contentType = null;


    protected String protocol = null;

    protected String scheme = null;



    // ----------------------------------------- implements Request

    @Override
    public String getAuthorization() {
        return null;
    }

    @Override
    public void setAuthorization(String authorization) {

    }

    @Override
    public void setContentLength(int length) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setProtocol(String protocol) {

    }

    @Override
    public void setRemoteAddr(String remote) {

    }

    @Override
    public void setScheme(String scheme) {

    }

    @Override
    public InputStream getStream() {
        return this.input;
    }

    @Override
    public void setStream(InputStream input) {
        this.input = input;
    }

    @Override
    public ServletRequest getRequest() {
        return facade;
    }

    @Override
    public Response getResponse() {
        return null;
    }

    @Override
    public void setResponse(Response response) {

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










    // --------------------------------------------- implements ServletRequest



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

        //return this.characterEncoding;
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        if (reader != null) {

        }

        if (stream == null) {
            stream = new RequestStream(this);
        }

        return this.stream;
    }

    @Override
    public String getParameter(String s) {
        return null;
    }

    @Override
    public Enumeration getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    @Override
    public Map getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public BufferedReader getReader() throws IOException {

        if (reader == null) {

            String encoding = getCharacterEncoding();
            if (encoding == null) {
                encoding = "ISO-8859-1";
            }

            InputStreamReader isr =
                    new InputStreamReader(createInputStream(), encoding);

            reader = new BufferedReader(isr);
        }

        return this.reader;
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
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
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




}
