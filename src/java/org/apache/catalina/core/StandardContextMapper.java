package org.apache.catalina.core;

import org.apache.catalina.*;
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
            name = context.findServletMapping(relativeURI); // servlet name
            if (name != null) {
                wrapper = (Wrapper) context.findChild(name);
            }
            if (wrapper != null) {
                servletPath = relativeURI;
                pathInfo = null;
            }
        }

        if (wrapper == null) {
            servletPath = relativeURI;
            while (true) {
                name = context.findServletMapping(servletPath + "/*");
                if (name != null) {
                    wrapper = (Wrapper) context.findChild(name);
                }
                if (wrapper != null) {
                    pathInfo = relativeURI.substring(servletPath.length());
                    if (pathInfo.length() == 0)
                        pathInfo = null;
                    break;
                }
                int slash = servletPath.lastIndexOf('/');
                if (slash < 0) {
                    break;
                }
                servletPath = servletPath.substring(0, slash);
            }
        }


        if (wrapper == null) {
            int slash = relativeURI.lastIndexOf('/');
            if (slash >= 0) {
                String last = relativeURI.substring(slash);
                int period = last.lastIndexOf('.');
                if (period >= 0) {
                    String pattern = "*" + last.substring(period);
                    name = context.findServletMapping(pattern);
                    if (name != null)
                        wrapper = (Wrapper) context.findChild(name);
                    if (wrapper != null) {
                        servletPath = relativeURI;
                        pathInfo = null;
                    }
                }
            }
        }

        if (wrapper == null) {
            name = context.findServletMapping("/");
            if (name != null)
                wrapper = (Wrapper) context.findChild(name);
            if (wrapper != null) {
                servletPath = relativeURI;
                pathInfo = null;
            }
        }

        if (update) {
            request.setWrapper(wrapper);
            ((HttpRequest) request).setServletPath(servletPath);
            ((HttpRequest) request).setPathInfo(pathInfo);
        }
        return wrapper;
    }
}
