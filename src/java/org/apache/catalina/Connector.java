package org.apache.catalina;

import org.apache.catalina.net.ServerSocketFactory;
import org.apache.catalina.util.LifecycleMBeanBase;

/**
 * Created by tisong on 8/9/16.
 */
public interface Connector extends LifecycleMBeanBase{


    // ------------------------------------------------ Properties

    public Container getContainer();

    public void setContainer(Container container);


    public ServerSocketFactory getFactory();

    public void setFactory(ServerSocketFactory factory);

    // ------------------------------------------------ Public Methods

    public void initialize() throws LifecycleException;

    public Request createRequest();

    public Response createResponse();
}
