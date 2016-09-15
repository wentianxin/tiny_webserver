package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.StringManager;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tisong on 9/4/16.
 */
public class StandardContextValve extends ValveBase{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);

    @Override
    public void invoke(Request request, Response response, ValueContext valueContext)
            throws IOException, ServletException {

        // 获取 RequestFacade 门面
        HttpServletRequest hreq = (HttpServletRequest) request.getRequest();

        String contextPath = hreq.getContextPath();
        String requestURI = hreq.getRequestURI();
        String relativeURI = requestURI.substring(contextPath.length()).toLowerCase();

        if (relativeURI.equals("/META-INF")
         || relativeURI.equals("/WEB-INF")
         || relativeURI.startsWith("/META-INF")
         || relativeURI.startsWith("/WEB-INF") ) {
            ((HttpServletResponse)response).sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
            return ;
        }


        StandardContext context = (StandardContext) getContainer();

        String sessionId = hreq.getRequestedSessionId();
        if (sessionId != null) {
            Manager manager = context.getManager();
            if (manager != null) {
                Session session = manager.findSession(sessionId);
                if (session != null && session.isVaild()) {
                    session.access();
                }
            }
        }

        Wrapper wrapper = (Wrapper) context.map(request, true);
        if (wrapper == null) {
            ((HttpServletResponse)response).sendError(HttpServletResponse.SC_NOT_FOUND, requestURI);
            return ;
        }


        //response.setContext(context);

        wrapper.invoke(request, response);
    }
}
