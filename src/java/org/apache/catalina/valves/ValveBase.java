package org.apache.catalina.valves;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 9/4/16.
 */
public abstract class ValveBase
        implements Contained, Value{

    private Container container = null;


    private static final String info =
            "org.apache.catalina.core.ValveBase/1.0";

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public abstract void invoke(Request request, Response response, ValueContext valueContext)
            throws IOException, ServletException;
}
