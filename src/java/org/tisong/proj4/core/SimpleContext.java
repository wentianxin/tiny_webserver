package org.tisong.proj4.core;

import org.apache.catalina.*;
import org.apache.catalina.Container;
import org.apache.catalina.util.LifecycleSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tisong on 8/9/16.
 */
public class SimpleContext implements Context, Lifecycle, Pipeline {

    private boolean started;

    private Loader loader;
    private Mapper mapper;
    private Container parent;
    private Pipeline pipeline;
    private Map children;
    private Map mappers;
    private Map servletMappings;
    private LifecycleSupport lifecycle;


    public SimpleContext() {
        this.started = false;
        this.lifecycle = new LifecycleSupport();
        this.pipeline = new SimplePipeline();
        this.mappers = new HashMap();
        this.servletMappings = new HashMap();
        this.children = new HashMap();
    }

    @Override
    public void start() throws LifecycleException {

        if (started) {
            throw new LifecycleException("SimpleContext has alirady started");
        }

        lifecycle.fireLifecycleEvent(BEFORE_START_EVENT, null);

        started = true;

        try {
            ((Lifecycle)loader).start();

            Container[] children = findChildren();
            for (int i = 0; i < children.length; i++) {
                ((Lifecycle)children[i]).start();
            }

            ((Lifecycle)pipeline).start();

            lifecycle.fireLifecycleEvent(START_EVENT, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        lifecycle.fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    @Override
    public void stop() throws LifecycleException {

    }


    @Override
    public void setName(String name) {

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
    public void addChild(Container child) {
        child.setParent(this);
        children.put(child.getName(), child);
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
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycle.addLifecycleListener(listener);
    }

    @Override
    public LifecycleListener[] findLifecycleListener() {
        return new LifecycleListener[0];
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void addMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void addLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public Loader getLoader() {
        return null;
    }

    @Override
    public void addServletMapping(String pattern, String name) {
        servletMappings.put(pattern, name);
    }
}
