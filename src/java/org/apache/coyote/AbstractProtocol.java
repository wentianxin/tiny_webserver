package org.apache.coyote;

import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.SocketStatus;
import org.apache.tomcat.util.net.SocketWrapper;

import java.io.IOException;

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

                Processor<S> processor = connections.remove(socket.getSocket());

                if (status == SocketStatus.DISCONNECT && processor == null) {
                    return SocketState.CLOSED;
                }

                socket.setAsync(false);



                try {
                    if (processor.isAsync()) {

                    } else {
                        processor.process(socket);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
    }
}
