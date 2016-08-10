package org.tisong.proj4.core;

import org.apache.catalina.*;
import org.apache.catalina.Container;
import org.apache.catalina.util.LifecycleSupport;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * tomcat的四大容器之一: Context的实现
 * 启动流程: start() → loader.start()
 *                  → children containers start()  [子容器为 Wrapper]
 * http请求流程: invoke() → pipeline.invoke() → pipelineValueContext.invokeNext() → values.invoke()...... → base.invoke()
 *              在base 阀门的 invoke调用中, 会根据 request 请求对象匹配到 wrapper对象（子容器）, 然后调用 wrapper.invoke()
 *
 * Created by tisong on 8/9/16.
 */
public class SimpleContext implements Context, Lifecycle, Pipeline {

    private boolean started;

    private String name;

    private Loader loader;
    private Mapper mapper;                    // every context have a mapper (http-mapper)
    private Container parent;

    private Pipeline pipeline;                //
    private Value base;

    private Map<String, Container> children;  // name - value; just like : wrapper container's name - wrapper object
    private Map<String, Mapper>    mappers;
    private Map<String, String>    servletMappings;

    private LifecycleSupport lifecycle;


    public SimpleContext() {
        this.started = false;
        this.lifecycle = new LifecycleSupport();
        this.pipeline = new SimplePipeline(this);
        this.mappers = new HashMap<String, Mapper>();
        this.servletMappings = new HashMap<String, String>();
        this.children = new HashMap<String, Container>();
        /* 初始化阀门 */
        this.base = new SimpleContextValue();
        ((Contained)this.base).setContainer(this);
        this.pipeline.setBasic(this.base);
    }


    // -----------------------------------------------------  Implements Lifecycle
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


    // ----------------------------------------------------- Implements Context
    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }


    @Override
    public void setParent(Container container) {
        this.parent = parent;
    }

    @Override
    public Container getParent() {
        return this.parent;
    }

    @Override
    public void setParentClassLoader(ClassLoader parent) {

    }

    @Override
    public ClassLoader getParentClassLoader() {
        return null;
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
    public void addChild(Container child) {
        child.setParent(this);
        children.put(child.getName(), child);
    }

    @Override
    public void removeChild(Container child) {

    }

    @Override
    public Container findChild(String name) {
        if (name == null)
            return null;

        return (Container)children.get(name);
    }

    @Override
    public Container[] findChildren() {
        return new Container[0];
    }

    /*  mapper 操作 */

    @Override
    public void addMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void removeMapper(Mapper mapper) {

    }

    @Override
    public Mapper findMapper(String protocol) {
        if (mapper != null) {
            return mapper;
        } else {
            // TODO return mappers.get
            return mappers.get(protocol);
        }
    }

    @Override
    public Mapper[] findMappers() {
        return new Mapper[0];
    }


    @Override
    public Container map(Request request, boolean update) {
        // TODO
        Mapper mapper = findMapper(request.getRequest().getProtocol());
        if (mapper == null) {
            return null;
        }

        return mapper.map(request, update);
    }


    /* ServletMapping 操作 */
    @Override
    public void addServletMapping(String pattern, String name) {
        this.servletMappings.put(pattern, name);
    }

    @Override
    public String findServletMapping(String pattern) {
        return this.servletMappings.get(pattern);
    }

    @Override
    public String[] findServletMappings() {
        return null;
    }


    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }



    // ------------------------------------------------------------ Implements Pipeline

    @Override
    public Value getBasic() {
        return this.pipeline.getBasic();
    }

    @Override
    public void setBasic(Value value) {
        this.pipeline.setBasic(value);
    }

    @Override
    public Value[] getValues() {
        return this.pipeline.getValues();
    }

    @Override
    public void addValue(Value value) {
        this.pipeline.addValue(value);
    }

    @Override
    public void removeValue(Value value) {
        this.pipeline.removeValue(value);
    }
}
