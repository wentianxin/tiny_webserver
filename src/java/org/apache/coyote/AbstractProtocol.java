package org.apache.coyote;

import org.apache.tomcat.util.net.AbstractEndpoint;

/**
 * Created by tisong on 8/21/16.
 */
public abstract class AbstractProtocol implements ProtocolHandler{

    protected AbstractEndpoint endpoint = null;

    @Override
    public void init() throws Exception {


        endpoint.init();
    }


    protected abstract static class AbstractConnectionHandler<S,P extends  Processor<S>>
        implements AbstractEndpoint.Handler {

            public SocketState process(SocketWrapper<S> socket,
                                       SocketStatus status) {

            }
        }
    }
}
