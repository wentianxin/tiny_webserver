package org.apache.catalina.core;

import org.apache.catalina.Loader;
import org.apache.catalina.Wrapper;

import javax.servlet.*;
import java.util.Enumeration;

/**
 * Created by tisong on 9/3/16.
 */
public class StandardWrapper extends ContainerBase
    implements Wrapper, ServletConfig{

    private long available = 0L;

    private Servlet servlet = null;

    private String servletClass = null;


    private InstanceSupport instanceSupport = new InstanceSupport(this);


    private boolean allocated = false;

    private boolean singelThreadModel = false;

    private int countAllocated = 0;


    public StandardWrapper() {
        super();
        pipeline.setBasic(new StandardWrapperValve());
    }

    /**
     * 线程安全问题: 可能会出现多个线程同时访问allocate的情况
     * @return
     * @throws ServletException
     */
    @Override
    public Servlet allocate() throws ServletException {

        if (servlet == null) {
            load();
        }

        if (!singelThreadModel) {
            countAllocated++;
            return servlet;
        } else {
            synchronized (servlet) {
                if (allocated) {
                    try {
                        servlet.wait();
                    } catch (InterruptedException e) {

                    }
                }
                allocated = true;
                countAllocated++;
                return servlet;
            }
        }
    }

    @Override
    public void deallocate(Servlet servlet) throws ServletException {

    }


    @Override
    public synchronized void load() {

        if (servlet != null) {
            return ;
        }

        Loader loader = getLoader();
        if (loader == null) {

        }

        ClassLoader classLoader = loader.getClassLoader();

        Class classClass = null;
        try {
            if (classLoader != null) {
                classClass = classLoader.loadClass(servletClass);
            } else {
                classClass = Class.forName(servletClass);
            }
        } catch (ClassNotFoundException e) {

        }

        Servlet instance = null;
        try {
            instance = (Servlet) classClass.newInstance();
        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {

        }

        servlet.init(facade);


        servlet = instance;
        singelThreadModel = servlet instanceof SingleThreadModel;


        fireContainerEvent("load", this);
    }

    @Override
    public void unload() throws ServletException {

    }


    @Override
    public long getAvailable() {
        return this.available;
    }

    @Override
    public void setAvailable(long available) {
        this.available = available;
    }

    @Override
    public boolean isUnavailable() {
        if (this.available == 0L) {
            return false;
        } else if (this.available <= System.currentTimeMillis()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    @Override
    public String getServletClass() {
        return this.servletClass;
    }


    // ---------------------------------- Implements ServletConfig
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
