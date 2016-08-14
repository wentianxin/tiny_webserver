package org.tisong.proj4.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 8/9/16.
 */
public class SimplePipeline implements Pipeline, Lifecycle, Contained{

    private Value   basic;
    private Value[] values = new Value[0];
    private Container container;


    public SimplePipeline(Container container) {
        this.container = container;
    }

    // -------------------------------------------------------------- Implements Lifecycle

    // TODO synchronized
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
    public Value getBasic() {
        return this.basic;
    }

    @Override
    public void setBasic(Value value) {
        this.basic = value;
    }

    @Override
    public Value[] getValues() {
        return values;
    }

    @Override
    public void addValue(Value value) {

    }

    @Override
    public void removeValue(Value value) {

    }

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }


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
