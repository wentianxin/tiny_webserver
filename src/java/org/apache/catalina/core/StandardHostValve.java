package org.apache.catalina.core;

import org.apache.catalina.Context;
import org.apache.catalina.Request;
import org.apache.catalina.Response;
import org.apache.catalina.ValueContext;
import org.apache.catalina.util.StringManager;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tisong on 9/4/16.
 */
public class StandardHostValve extends ValveBase{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);

    @Override
    public void invoke(Request request, Response response, ValueContext valueContext) throws IOException, ServletException {

//        if (!(request.getRequest() instanceof HttpServletRequest) ||
//                !(response.getResponse() instanceof HttpServletResponse)) {
//            return;     // NOTE - Not much else we can do generically
//        }

        StandardHost host = (StandardHost) getContainer();
        Context context = (Context) host.map(request, true);
        if (context == null) {
            ((HttpServletResponse) response.getResponse()).sendError
                    (HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            sm.getString("standardHost.noContext"));
            return;
        }

//        Thread.currentThread().setContextClassLoader
//                (context.getLoader().getClassLoader());

        context.invoke(request, response);
    }
}
