package org.apache.catalina.core;

import org.apache.catalina.Container;
import org.apache.catalina.Mapper;
import org.apache.catalina.Request;
import org.apache.catalina.Wrapper;
import org.apache.catalina.util.StringManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tisong on 9/5/16.
 */
public class StandardContextMapper
    implements Mapper{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);

    private StandardContext context = null;

    private String protocol = null;

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public Container getContainer() {
        return this.context;
    }

    @Override
    public void setContainer(Container container) {
        if (!(container instanceof StandardContext))
            throw new IllegalArgumentException
                    (sm.getString("httpContextMapper.container"));
        context = (StandardContext) container;

    }

    @Override
    public Container map(Request request, boolean update) {


        String contextPath =
                ((HttpServletRequest) request.getRequest()).getContextPath();
        String requestURI =
                ((HttpServletRequest) request.getRequest()).getRequestURI();
        String relativeURI = requestURI.substring(contextPath.length());


        Wrapper wrapper = null;
        String servletPath = relativeURI;
        String pathInfo = null;
        String name = null;

        if (wrapper == null) {
            name = context.findServletMapping(relativeURI);
            if (name != null) {
                wrapper = (Wrapper) context.findChild(name);
            }
            if (wrapper != null) {
                servletPath = relativeURI;
                pathInfo = null;
            }
        }

        return wrapper;
    }
}
