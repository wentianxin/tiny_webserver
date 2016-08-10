package org.tisong.proj4.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by tisong on 8/9/16.
 */
public class SimpleContextValue implements Value{
    @Override
    public void invoke(Request request, Response response, ValueContext valueContext) throws IOException, ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getRequest();

        Context context = (Context) getContainer();

        Wrapper wrapper = null;

        wrapper = (Wrapper) context.map(request, true);

        if (wrapper == null) {

            return ;
        }

        wrapper.invoke(request, response);
    }
}
