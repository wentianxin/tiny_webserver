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


    protected int contentCount = 0;


    protected Request request = null;

    protected Connector connector = null;


    /**
     * 关联的 输出流(Socket输出流)
     */
    protected OutputStream output = null;


    protected ServletOutputStream stream = null;

    protected PrintWriter writer = null;




    protected ResponseFacade facade = new ResponseFacade(this);


    /**
     * RequestDispatcher.include();
     */
    protected boolean included = false;

    protected boolean committed = false;




    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }


    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (stream == null) {
            stream = createOutputStream();
        }


        return this.stream;
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

        writer = new ResponseWriter(osr, (ResponseStream) stream);

        return writer;
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.characterEncoding = charset;
    }

    @Override
    public void setContentLength(int len) {
        this.contentLength = len;
    }

    @Override
    public void setContentType(String type) {
        this.contentType = type;
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
        return this.connector;
    }

    @Override
    public void setConnector(Connector connector) {
        this.connector = connector;
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
        return this.request;
    }

    @Override
    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public ServletResponse getResponse() {
        return this.facade;
    }

    @Override
    public OutputStream getStream() {
        return null;
    }

    @Override
    public void setStream(OutputStream stream) {
        this.output = stream;
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
