package org.apache.catalina.core;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * Created by tisong on 9/4/16.
 */
public class ApplicationFilterConfig implements FilterConfig {

    private Filter filter = null;

    @Override
    public String getFilterName() {
        return null;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @Override
    public Enumeration getInitParameterNames() {
        return null;
    }


    public Filter getFilter() {

        if (this.filter != null) {
            return this.filter;
        }


    }

}
