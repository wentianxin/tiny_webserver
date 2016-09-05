package org.apache.catalina.core;

import org.apache.catalina.Host;
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
public class StandardEngineValve extends ValveBase{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);


    @Override
    public void invoke(Request request, Response response, ValueContext valueContext) throws IOException, ServletException {
        // TODO 为什么要在这里检验
//        if (!(request.getRequest() instanceof HttpServletRequest) ||
//                !(response.getResponse() instanceof HttpServletResponse)) {
//            return;
//        }

        StandardEngine engine = (StandardEngine) getContainer();

        Host host = (Host) engine.map(request, true);
        if (host == null) {
            ((HttpServletResponse) response.getResponse()).sendError
                    (HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            sm.getString("standardEngine.noHost",
                                    request.getRequest().getServerName()));
            return;
        }

        host.invoke(request, response);
    }
}
