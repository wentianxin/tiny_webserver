package org.apache.catalina.connector;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Created by tisong on 9/4/16.
 */
public class ResponseWriter extends PrintWriter{

    protected ResponseStream stream = null;

    public ResponseWriter(OutputStreamWriter writer, ResponseStream stream) {

        super(writer);
        this.stream = stream;
        //this.stream.setCommit(false);

    }
}
