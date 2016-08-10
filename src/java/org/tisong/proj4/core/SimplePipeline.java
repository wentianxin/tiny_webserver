package org.tisong.proj4.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 8/9/16.
 */
public class SimplePipeline implements Pipeline, Lifecycle{

    private Value   basic;
    private Value[] values = new Value[0];



    // -------------------------------------------------------------- Implements Lifecycle
    @Override
    public void start() throws LifecycleException {

    }

    @Override
    public void stop() throws LifecycleException {

    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListener() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }


    // --------------------------------------------------------------- Implements Pipeline


    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {

        new StandardPipelineValueContext().invokeNext(request, response);
    }

    class StandardPipelineValueContext implements ValueContext {
        private int stage = 0;

        @Override
        public void invokeNext(Request request, Response response) throws IOException, ServletException {

            int subscript = stage++;

            if (subscript < values.length) {
                values[subscript].invoke(request, response, this);
            } else if ( (subscript == values.length) && (basic != null) ) {
                basic.invoke(request, response, this);
            } else {
                throw new ServletException("No value");
            }
        }
    }
}
