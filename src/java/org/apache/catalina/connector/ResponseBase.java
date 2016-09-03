package org.apache.catalina.connector;

import org.apache.catalina.Connector;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.connector.http10.ResponseFacade;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by tisong on 9/3/16.
 */
public abstract class ResponseBase
        implements Response, ServletResponse{

    private Connector connector = null;

    protected ResponseFacade facade = new ResponseFacade(this);


    // ---------------------------------------- implements ServletResponse

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return null;
    }

    @Override
    public void setCharacterEncoding(String s) {

    }

    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }



    // ------------------------------------------ implements Response

    @Override
    public Request getRequest() {
        return null;
    }

    @Override
    public void setRequest(Request request) {

    }

    @Override
    public ServletResponse getResponse() {
        return null;
    }

    @Override
    public OutputStream getStream() {
        return null;
    }

    @Override
    public void setStream(OutputStream output) {

    }

    @Override
    public ServletOutputStream createOutputStream() throws IOException {
        return null;
    }

    @Override
    public void finshResponse() throws IOException {

    }

    @Override
    public int getContextLength() {
        return 0;
    }


}
