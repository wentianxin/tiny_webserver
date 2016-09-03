package org.apache.catalina;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tisong on 8/10/16.
 */
public interface Request {

    // ------------------------------------------------------------- Properties


    public String getAuthorization();

    public void setAuthorization(String authorization);


    public void setContentLength(int length);


    public void setContentType(String type);


    public void setProtocol(String protocol);


    public void setRemoteAddr(String remote);


    public void setScheme(String scheme);



    public InputStream getStream();


    public void setStream(InputStream stream);



    public ServletRequest getRequest();


    public Response getResponse();

    public void setResponse(Response response);



    // -------------------------------------------------- public methods

    public ServletInputStream createInputStream() throws IOException;

    public void finishRequest() throws IOException;




}
