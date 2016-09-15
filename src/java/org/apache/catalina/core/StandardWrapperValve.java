package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.StringManager;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tisong on 9/4/16.
 */
public class StandardWrapperValve extends ValveBase{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);


    @Override
    public void invoke(Request request, Response response, ValueContext valueContext)
            throws IOException, ServletException {

        ServletRequest sreq = request.getRequest();
        ServletResponse sres = response.getResponse();

        HttpServletRequest hreq = null;
        HttpServletResponse hres = null;

        if (sreq instanceof HttpServletRequest) {
            hreq = (HttpServletRequest) sreq;
        }
        if (sres instanceof HttpServletResponse) {
            hres = (HttpServletResponse) sres;
        }


        boolean unavailable = false;

        StandardWrapper wrapper = (StandardWrapper) getContainer();

        /* 检查父容器是否可用 */
        if (!((Context) wrapper.getParent()).isAvailable()) {
            if (hres != null) {
                hres.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                        sm.getString("standardContext.isUnavailable"));
            }
            unavailable = true;
        }

        /* 检查自身绑定容器是否可用 */
        if (!unavailable && wrapper.isUnavailable()) {
            log(sm.getString("standardWrapper.isUnavailable", wrapper.getName()));

            if (hres != null) {

                hres.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                        sm.getString("standardWrapper.isUnavailable", wrapper.getName()));
            }
            unavailable = true;
        }

        Servlet servlet = null;
        try {
            if (!unavailable) {
                servlet = wrapper.allocate();
            }
        } catch (ServletException e) {
            log(sm.getString("standardWrapper.allocateException",
                    wrapper.getName()), e);
            servlet = null;
        }

        ApplicationFilterChain filterChain = createFilterChain(request, servlet);

        try {
            if (servlet != null && filterChain != null) {
                filterChain.doFilter(sreq, sres);
            }
        } catch (IOException e) {

        } catch (ServletException e) {

        }

        try {
            // servlet 调用完毕
            if (filterChain != null) {
                filterChain.release();
            }
        } catch (Throwable t) {

        }

        try {
            if (servlet != null) {
                wrapper.deallocate(servlet);
            }
        } catch (Throwable t) {

        }

        try {
            if (servlet != null && wrapper.getAvailable() == Long.MAX_VALUE) {
                wrapper.unload();
            }
        } catch (Throwable t) {

        }

    }



    private ApplicationFilterChain createFilterChain(Request request,
                                                     Servlet servlet) {
        return null;
    }


    private void log(String message) {

    }

    private void log(String message, Throwable throwable) {

    }
}
