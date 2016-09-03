package org.apache.catalina;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tisong on 8/10/16.
 */
public interface Response {











    public Request getRequest();


    public void setRequest(Request request);



    public ServletResponse getResponse();


    public OutputStream getStream();


    public void setStream(OutputStream output);



    // --------------------------------------------------------- Public Methods

    public ServletOutputStream createOutputStream() throws IOException;


    public void finshResponse() throws IOException;


    public int getContextLength();



    public String getContentType();



}
