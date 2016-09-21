package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 9/4/16.
 */
public class StandardPipeline
    implements Pipeline, Lifecycle, Contained{

    protected static StringManager sm =
            StringManager.getManager(Constants.Package);


    private Container container = null;

    protected Value basic = null;

    protected Value[] values = new Value[0];


    private boolean started = false;

    private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);


    public StandardPipeline(Container container) {
        this.container = container;
    }


    // ----------------------------------------- implements Contained

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }


    // ----------------------------------------- implements Lifecycle

    @Override
    public void start() throws LifecycleException {

        if (started) {

        }

        lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);

        started = true;

        for (Value value: values) {
            if (value instanceof Lifecycle) {
                ((Lifecycle) value).start();
            }
        }

        if (basic != null && basic instanceof Lifecycle) {
            ((Lifecycle) basic).start();
        }

        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

        lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    @Override
    public void stop() throws LifecycleException {

    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.addLifecycleListener(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.removeLifecycleListener(listener);
    }



    // ----------------------------------------- implements Pipeline
    @Override
    public Value getBasic() {
        return this.basic;
    }

    @Override
    public void setBasic(Value value) {

        if (value instanceof Contained) {
            ((Contained) value).setContainer(this.container);
        }
        if (value instanceof Lifecycle) {
            try {
                ((Lifecycle) value).start();
            } catch (LifecycleException e) {
                e.printStackTrace();
                return ;
            }
        }

        this.basic = value;
    }

    @Override
    public Value[] getValues() {
        return this.values;
    }

    @Override
    public void addValue(Value value) {

        if (value instanceof Contained) {
            ((Contained) value).setContainer(container);
        }

        if (started && value instanceof Lifecycle) {
            try {
                ((Lifecycle) value).start();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }

        Value[] results = new Value[values.length + 1];

        System.arraycopy(values, 0, results, 0, values.length);

        results[values.length] = value;

        values = results;
    }

    @Override
    public void removeValue(Value value) {

    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        (new StandardPipelineValveContext()).invokeNext(request, response);
    }


    protected class StandardPipelineValveContext implements ValueContext {


        protected int stage = 0;

        @Override
        public void invokeNext(Request request, Response response) throws IOException, ServletException {
            int subscript = stage;
            stage = stage + 1;

            /**
             * 整个流水线的核心线就是 basic; 互相连接着 一直到过滤器; 然后再逐个返回
             */
            if (subscript < values.length) {
                values[subscript].invoke(request, response, this);
            } else if ((subscript == values.length) && (basic != null)) {
                basic.invoke(request, response, this);
            } else {
                throw new ServletException
                        (sm.getString("standardPipeline.noValve"));
            }
        }
    }
}
