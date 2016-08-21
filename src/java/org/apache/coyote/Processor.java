package org.apache.coyote;

import java.io.IOException;

/**
 * Common interface for processors of all protocols.
 */
public interface Processor<S> {

    SocketState process(SocketWrapper<S> socketWrapper) throws IOException;
}
