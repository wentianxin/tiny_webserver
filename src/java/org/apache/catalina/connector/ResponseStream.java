package org.apache.catalina.connector;

import org.apache.catalina.Response;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * Created by tisong on 9/4/16.
 */
public class ResponseStream extends ServletOutputStream{


    public ResponseStream(Response response) {

    }


    @Override
    public void write(int b) throws IOException {

    }
}
