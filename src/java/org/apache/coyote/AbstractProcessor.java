package org.apache.coyote;

import java.io.IOException;
import java.util.concurrent.Executor;

import org.apache.coyote.http11.upgrade.UpgradeInbound;
import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
import org.apache.tomcat.util.net.SocketStatus;
import org.apache.tomcat.util.net.SocketWrapper;
/**
 * Created by tisong on 8/22/16.
 */
public abstract class AbstractProcessor<S> implements ActionHook, Processor<S>{

    protected Adapter adapter;


    protected Request request;

    protected Response response;


    protected AsyncStateMachine<S> asyncStateMachine;


    protected AbstractEndpoint endpoint;

    public AbstractProcessor(AbstractEndpoint endpoint) {

        this.endpoint = endpoint;

        asyncStateMachine = new AsyncStateMachine<S>(this);

        request = new Request();

        response = new Response();

        request.setHook(this);

        request.setResponse(response);


    }

    @Override
    public abstract SocketState process(SocketWrapper<S> socket)
            throws IOException;






    /**
     * Obtain the Executor used by the underlying endpoint.
     */
    @Override
    public Executor getExecutor() {
        return endpoint.getExecutor();
    }


    @Override
    public boolean isAsync() {
        return (asyncStateMachine != null && asyncStateMachine.isAsync());
    }
}
