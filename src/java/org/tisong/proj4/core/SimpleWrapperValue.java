package org.tisong.proj4.core;

import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.Value;
import org.apache.catalina.ValueContext;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 8/9/16.
 */
public class SimpleWrapperValue implements Value{


    @Override
    public void invoke(Request request, Response response, ValueContext valueContext) throws IOException, ServletException {

        SimpleWrapper wrapper = (SimpleWrapper) getContainer();

        Servlet servlet = null;

        servlet = wrapper.allocate();

        servlet.service(request, response);
    }
}
