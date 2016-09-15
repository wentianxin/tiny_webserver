package org.apache.catalina.core;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;

/**
 * Created by tisong on 9/5/16.
 */
public class StandardWrapperFacade implements ServletConfig{


    private ServletConfig config = null;


    public StandardWrapperFacade (StandardWrapper config) {

        this.config = config;
    }

    @Override
    public String getServletName() {
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
}
