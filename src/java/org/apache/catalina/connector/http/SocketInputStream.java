package org.apache.catalina.connector.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tisong on 8/10/16.
 */
public class SocketInputStream extends InputStream{

    @Override
    public int read() throws IOException {
        return 0;
    }
}
