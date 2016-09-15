package org.apache.catalina;

import org.apache.catalina.net.ServerSocketFactory;

/**
 * Created by tisong on 8/9/16.
 */
public interface Connector {


    // ------------------------------------------------ Properties

    public Container getContainer();

    public void setContainer(Container container);


    public ServerSocketFactory getFactory();

    public void setFactory(ServerSocketFactory factory);

    public String getScheme();

    public void setScheme(String scheme);


    public String getInfo();

    // ------------------------------------------------ Public Methods

    public void initialize() throws LifecycleException;

    public Request createRequest();

    public Response createResponse();


}
