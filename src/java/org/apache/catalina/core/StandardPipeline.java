package org.apache.catalina.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 9/4/16.
 */
public class StandardPipeline
    implements Pipeline, Lifecycle, Contained{


    private Container container = null;

    public StandardPipeline(Container container) {
        this.container = container;
    }


    // ----------------------------------------- implements Contained

    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {

    }


    // ----------------------------------------- implements Lifecycle

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



    // ----------------------------------------- implements Pipeline
    @Override
    public Value getBasic() {
        return null;
    }

    @Override
    public void setBasic(Value value) {

    }

    @Override
    public Value[] getValues() {
        return new Value[0];
    }

    @Override
    public void addValue(Value value) {

    }

    @Override
    public void removeValue(Value value) {

    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {

    }
}
