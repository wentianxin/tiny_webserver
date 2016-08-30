package org.apache.coyote;

import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
import org.apache.tomcat.util.net.SocketWrapper;

import java.io.IOException;

/**
 * Common interface for processors of all protocols.
 */
public interface Processor<S> {

    SocketState process(SocketWrapper<S> socketWrapper) throws IOException;
}
