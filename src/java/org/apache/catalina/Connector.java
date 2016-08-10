package org.apache.catalina;

/**
 * Created by tisong on 8/9/16.
 */
public interface Connector {

    // ------------------------------------------------ Public Methods

    public void initialize() throws LifecycleException;

    public Request createRequest();

    public Response createResponse();
}
