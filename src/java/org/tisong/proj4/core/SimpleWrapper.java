package org.tisong.proj4.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 8/9/16.
 */
public class SimpleWrapper implements Wrapper, Pipeline, Lifecycle{

    private Servlet servlet;
    private String  servletClass;
    private String  name;
    private boolean started;
    private Container parent;
    private Pipeline pipeline;
    private Loader  loader;
    private LifecycleSupport lifecycle;


    public SimpleWrapper() {
        this.pipeline = new SimplePipeline();
        this.pipeline.setBasic(new SimpleWrapperValue());
    }

    @Override
    public void start() throws LifecycleException {
        if (started) {
            throw new LifecycleException("Wrapper already started");
        }

        System.out.println("Starting Wrapper " + name);

        started = true;

        lifecycle.fireLifecycleEvent(BEFORE_START_EVENT, null);

        if (loader != null && loader instanceof Lifecycle) {
            ((Lifecycle) loader).start();
        }

        if (pipeline != null && pipeline instanceof Lifecycle) {
            ((Lifecycle) pipeline).start();
        }

        lifecycle.fireLifecycleEvent(START_EVENT, null);
        lifecycle.fireLifecycleEvent(AFTER_START_EVENT, null);

    }

    @Override
    public void stop() throws LifecycleException {

    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public LifecycleListener[] findLifecycleListener() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setParent(Container container) {

    }

    @Override
    public Container getParent() {
        return null;
    }

    @Override
    public void setParentClassLoader(ClassLoader parent) {

    }

    @Override
    public ClassLoader getParentClassLoader() {
        return null;
    }

    @Override
    public void addChild(Container child) {

    }

    @Override
    public void removeChild(Container child) {

    }

    @Override
    public Container findChild(String name) {
        return null;
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }

    @Override
    public void addMapper(Mapper mapper) {

    }

    @Override
    public void removeMapper(Mapper mapper) {

    }

    @Override
    public Mapper findMapper(String protocol) {
        return null;
    }

    @Override
    public Mapper[] findMappers() {
        return new Mapper[0];
    }

    @Override
    public Container map(Request request, boolean update) {
        return null;
    }

    @Override
    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    @Override
    public String getServletClass() {
        return this.servletClass;
    }


    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }
}
