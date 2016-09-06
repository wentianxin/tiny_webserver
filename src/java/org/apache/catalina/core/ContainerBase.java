package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

import javax.naming.directory.DirContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tisong on 9/3/16.
 */
public class ContainerBase
    implements Container, Lifecycle, Pipeline{

    protected static StringManager sm =
            StringManager.getManager(Constants.Package);

    /**
     * 所有容器均会拥有的流水线
     */
    protected Pipeline pipeline = new StandardPipeline(this);


    protected Loader loader = null;

    protected Manager manager = null;

    protected Mapper mapper = null;

    protected Map<String, Mapper> mappers = new HashMap<String, Mapper>();

    protected Map<String, Container> children = new HashMap<String, Container>();

    protected Container parent = null;  // 可能会用到 parent loader

    protected ClassLoader parentClassLoader = null;


    protected String name = null;


    protected LifecycleSupport lifecycleSupport = new LifecycleSupport(this);


    protected DirContext resources = null;

    protected boolean started = false;


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setResources(DirContext resources) {
        this.resources = resources;
    }

    public DirContext getResources() {
        return resources;
    }

    @Override
    public void setParent(Container container) {
        this.parent = container;
    }

    @Override
    public Container getParent() {
        return this.parent;
    }

    @Override
    public void setParentClassLoader(ClassLoader parent) {
        this.parentClassLoader = parent;
    }

    @Override
    public ClassLoader getParentClassLoader() {
        return this.parentClassLoader;
    }


    @Override
    public Manager getManager() {
        if (manager != null)
            return manager;
        if (parent != null)
            return parent.getManager();
        return null;
    }

    @Override
    public void setManager(Manager manager) {

    }

    @Override
    public Loader getLoader() {
        if (loader != null) {
            return loader;
        } else if (parent != null) {
            return parent.getLoader();
        }

        return null;
    }


    @Override
    public void setLoader(Loader loader) {
        this.loader = loader;
    }

    @Override
    public void addChild(Container child) {

        child.setParent(this);

        children.put(child.getName(), child);

        // TODO fireContainerEvent(ADD_CHILD_EVENT, child);
    }

    @Override
    public void removeChild(Container child) {

        children.remove(child.getName());

        child.setParent(null);

        // TODO fireContainerEvent(REMOVE_CHILD_EVENT, child);
    }

    @Override
    public Container findChild(String name) {

        // TODO 需要锁住 children?
        if (name == null || children == null) {
            return null;
        }

        return children.get(name);
    }

    @Override
    public Container[] findChildren() {

        if (children == null) {
            return null;
        }

        Container[] c = new Container[children.size()];

        return children.values().toArray(c);
    }

    @Override
    public void addMapper(Mapper mapper) {

        synchronized (mappers) {

            mapper.setContainer(this);

            mappers.put(mapper.getProtocol(), mapper);

            this.mapper = (mappers.size() == 1) ? mapper : null;

            // TODO fireContainerEvent(ADD_MAPPER_EVENT, null);
        }
    }

    @Override
    public void removeMapper(Mapper mapper) {

        if (mappers.get(mapper.getProtocol()) == null) {
            return ;
        }

        synchronized (mappers) {
            mapper.setContainer(null);

            mappers.remove(mapper.getProtocol());


            this.mapper = ( ( mappers.size() == 1) ?
                            ( mappers.values().iterator().next()) :
                            ( null) );

            // TODO fireContainerEvent(REMOVE_MAPPER_EVENT, mapper);
        }
    }

    @Override
    public Mapper findMapper(String protocol) {

        if (mapper != null) {
            return mapper;
        } else {
            return mappers.get(protocol);
        }
    }

    @Override
    public Mapper[] findMappers() {
        if (mappers == null) {
            return null;
        }

        // Map to Array
        Mapper[] m = new Mapper[mappers.size()];

        return mappers.values().toArray(m);
    }

    @Override
    public Container map(Request request, boolean update) {

        Mapper mapper = findMapper(request.getRequest().getProtocol());
        if (mapper == null) {
            return null;
        }

        return mapper.map(request, update);
    }




    // ----------------------------------------- implements Pipeline

    @Override
    public Value getBasic() {
        return pipeline.getBasic();
    }

    @Override
    public void setBasic(Value value) {
        pipeline.setBasic(value);
    }


    @Override
    public Value[] getValues() {
        return pipeline.getValues();
    }

    @Override
    public void addValue(Value value) {
        pipeline.addValue(value);
    }

    @Override
    public void removeValue(Value value) {
        pipeline.removeValue(value);
    }


    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        pipeline.invoke(request, response);
    }





    // -------------------------------- implement Lifecycle

    /**
     * 启动相关联的组件
     * 启动相关Mapper
     * 启动子容器
     * 启动绑定的流水线
     * @throws LifecycleException
     */
    @Override
    public void start() throws LifecycleException {

        if (started) {
            throw new LifecycleException(
                    sm.getString("containerBase.alreadyStarted"));
        }

        started = true;


        if (loader != null && loader instanceof Lifecycle) {
            ((Lifecycle) loader).start();
        }
        if ((manager != null) && (manager instanceof Lifecycle)) {
            ((Lifecycle) manager).start();
        }

        Mapper mappers[] = findMappers();
        for (int i = 0; i < mappers.length; i++) {
            if (mappers[i] instanceof Lifecycle)
                ((Lifecycle) mappers[i]).start();
        }

        Container children[] = findChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Lifecycle)
                ((Lifecycle) children[i]).start();
        }

        if (pipeline instanceof Lifecycle) {
            ((Lifecycle) pipeline).start();
        }

        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
    }

    @Override
    public void stop() throws LifecycleException {

        if (!started) {
            throw new LifecycleException
                    (sm.getString("containerBase.notStarted"));
        }

        lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

        started = false;

        if (pipeline instanceof Lifecycle) {
            ((Lifecycle) pipeline).stop();
        }


        Container children[] = findChildren();
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Lifecycle)
                ((Lifecycle) children[i]).stop();
        }

        // Stop our Mappers, if any
        Mapper mappers[] = findMappers();
        for (int i = 0; i < mappers.length; i++) {
            if (mappers[(mappers.length-1)-i] instanceof Lifecycle)
                ((Lifecycle) mappers[(mappers.length-1)-i]).stop();
        }

        // Stop our subordinate components, if any
//        if ((resources != null) && (resources instanceof Lifecycle)) {
//            ((Lifecycle) resources).stop();
//        }
//        if ((realm != null) && (realm instanceof Lifecycle)) {
//            ((Lifecycle) realm).stop();
//        }
//        if ((cluster != null) && (cluster instanceof Lifecycle)) {
//            ((Lifecycle) cluster).stop();
//        }
        if ((manager != null) && (manager instanceof Lifecycle)) {
            ((Lifecycle) manager).stop();
        }
//        if ((logger != null) && (logger instanceof Lifecycle)) {
//            ((Lifecycle) logger).stop();
//        }
        if ((loader != null) && (loader instanceof Lifecycle)) {
            ((Lifecycle) loader).stop();
        }
    }


    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.addLifecycleListener(listener);
    }


    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        lifecycleSupport.removeLifecycleListener(listener);
    }



    // -----------------------


    public void fireContainerEvent(String type, Object data) {


    }
}
