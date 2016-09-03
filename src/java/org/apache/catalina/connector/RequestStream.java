package org.apache.catalina.connector;

import org.apache.catalina.Request;

import javax.servlet.ServletInputStream;
import java.io.IOException;

/**
 * Created by tisong on 9/3/16.
 */
public class RequestStream extends ServletInputStream{


    public RequestStream(Request request) {


    }

    @Override
    public int read() throws IOException {
        return 0;
    }
}
