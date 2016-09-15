package org.apache.catalina;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tisong on 8/10/16.
 */
public interface Response {

    // ------------------------------------ Properties

    public Connector getConnector();
    public void setConnector(Connector connector);

    public void setError();
    public boolean isError();

    /**
     * 关联的 Request对象
     */
    public Request getRequest();
    public void setRequest(Request request);


    public ServletResponse getResponse();


    public OutputStream getStream();
    public void setStream(OutputStream stream);


    // ---------------------------

    public ServletOutputStream createOutputStream() throws IOException;

    public void finishResponse() throws IOException;


    public void recycle();
}
