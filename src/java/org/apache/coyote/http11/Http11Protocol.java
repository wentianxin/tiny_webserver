package org.apache.coyote.http11;

import org.apache.tomcat.util.net.AbstractEndpoint;
import org.apache.tomcat.util.net.JIoEndpoint;

import java.net.Socket;

/**
 * Created by tisong on 8/21/16.
 */
public class Http11Protocol extends AbstractHttp11JsseProtocol{


    private Http11ConnectionHandler cHandler;

    public Http11Protocol() {

        endpoint = new JIoEndpoint();
        cHandler = new Http11ConnectionHandler(this);


    }

    protected static class Http11ConnectionHandler
        extends AbstractConnectionHandler<Socket, Http1Processor> implements AbstractEndpoint.Handler {

        protected Http11Protocol proto;

        Http11ConnectionHandler (Http11Protocol proto) {
            this.proto = proto;
        }
    }
}
