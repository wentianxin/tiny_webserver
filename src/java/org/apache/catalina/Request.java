package org.apache.catalina;

import javax.servlet.ServletRequest;

/**
 * Created by tisong on 8/10/16.
 */
public interface Request {

    // ------------------------------------------------------------- Properties

    public ServletRequest getRequest();

    public Response getResponse();

    public void setResponse(Response response);
}
