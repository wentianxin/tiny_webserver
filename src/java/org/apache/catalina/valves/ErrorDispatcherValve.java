package org.apache.catalina.valves;

import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.ValueContext;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 9/15/16.
 */
public class ErrorDispatcherValve extends ValveBase {
    @Override
    public void invoke(Request request, Response response, ValueContext valueContext) throws IOException, ServletException {

    }
}
