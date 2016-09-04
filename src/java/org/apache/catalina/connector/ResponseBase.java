package org.apache.catalina.connector;

import org.apache.catalina.Connector;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.util.StringManager;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Created by tisong on 9/4/16.
 */
public abstract class ResponseBase implements Response, ServletResponse{

    protected static StringManager sm =
            StringManager.getManager(Constants.Package);


    protected String characterEncoding = null;

    protected String contentType = null;

    protected int contentLength = -1;


    protected OutputStream output = null;

    protected ServletOutputStream stream = null;

    protected PrintWriter writer = null;


    protected Request request = null;

    protected ResponseFacade facade = new ResponseFacade(this);

    protected Connector connector = null;




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

        if (writer != null) {
            return writer;
        }

        if (stream != null) {

        }

        stream = createOutputStream();

        OutputStreamWriter osr =
                new OutputStreamWriter(stream, getCharacterEncoding());

        writer = new ResponseWriter(ors, (ResponseStream) stream);

        return writer;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public void setBufferSize(int size) {

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
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }



    // ------------------------------------ implements Response

    @Override
    public Connector getConnector() {
        return null;
    }

    @Override
    public void setConnector() {

    }

    @Override
    public void setError() {

    }

    @Override
    public boolean isError() {
        return false;
    }

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
    public void setSream(OutputStream stream) {

    }

    @Override
    public ServletOutputStream createOutputStream() throws IOException {
        return new ResponseStream(this);
    }

    @Override
    public void finishResponse() throws IOException {

    }

    @Override
    public void recycle() {

    }
}
