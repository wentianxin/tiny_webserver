package org.apache.catalina.core;

import org.apache.catalina.util.StringManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tisong on 9/4/16.
 */
public class ApplicationFilterChain implements FilterChain{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);


    private Iterator<ApplicationFilterConfig> iterator = null;

    private List filters = new ArrayList();

    private Servlet servlet = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

        internalDoFilter(request,response);
    }

    private void internalDoFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {

        if (iterator == null) {
            this.iterator = filters.iterator();
        }

        if (iterator.hasNext()) {
            ApplicationFilterConfig filterConfig = iterator.next();
            Filter filter = null;

            try {
                filter = filterConfig.getFilter();

                filter.doFilter(request, response, this);
            } catch (Exception e) {

            }
            return ;
        }

        try {

            if (request instanceof HttpServletRequest &&
            response instanceof HttpServletResponse) {
                servlet.service((HttpServletRequest)request, (HttpServletResponse)response);
            } else {
                servlet.service(request, response);
            }

        } catch (Exception e) {

        }
    }


    public void addFilter(ApplicationFilterConfig filterConfig) {

        this.filters.add(filterConfig);
    }

    public void setServlet(Servlet servlet) {
        this.servlet = servlet;
    }

    public Servlet getServlet() {
        return servlet;
    }

    public void release() {

        this.filters.clear();
        this.iterator = null;
        this.servlet = null;
    }

}
