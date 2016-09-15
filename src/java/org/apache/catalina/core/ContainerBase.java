package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

import javax.naming.directory.DirContext;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    protected Realm realm = null;

    protected Logger log = null;

    protected Mapper mapper = null;

    protected Map<String, Mapper> mappers = new HashMap<String, Mapper>();

    protected String mapperClass = null;

    protected Map<String, Container> children = new HashMap<String, Container>();

    protected Container parent = null;  // 可能会用到 parent loader

    protected ClassLoader parentClassLoader = null;


    protected String name = null;

    /**
     * Lifecycle Listener 的调用者
     */
    protected LifecycleSupport lifecycleSupport = new LifecycleSupport(this);

    /**
     * Container Listener
     */
    protected List<ContainerListener> containerListeners = new ArrayList<ContainerListener>();

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
        if (parentClassLoader != null) {
            return this.parentClassLoader;
        } else if (parent != null) {
            return parent.getParentClassLoader();
        } else {
            return ClassLoader.getSystemClassLoader();
        }
    }




    @Override
    public Realm getRealm() {
        return null;
    }

    @Override
    public void setRealm(Realm realm) {

    }

    @Override
    public Logger getLogger() {
        return null;
    }

    @Override
    public void setLogger(Logger logger) {

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

        try {
            ((Lifecycle)child).start();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }

        children.put(child.getName(), child);

        fireContainerEvent(ADD_CHILD_EVENT, child);
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


        lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);

        addDefaultMapper(this.mapperClass);

        started = true;

        if ((loader != null) && (loader instanceof Lifecycle)) {
            ((Lifecycle) loader).start();
        }
//        if ((logger != null) && (logger instanceof Lifecycle))
//            ((Lifecycle) logger).start();
        if ((manager != null) && (manager instanceof Lifecycle)) {
            ((Lifecycle) manager).start();
        }
//        if ((cluster != null) && (cluster instanceof Lifecycle))
//            ((Lifecycle) cluster).start();
//        if ((realm != null) && (realm instanceof Lifecycle))
//            ((Lifecycle) realm).start();
        if ((resources != null) && (resources instanceof Lifecycle)) {
            ((Lifecycle) resources).start();
        }

        Mapper[] mappers = findMappers(); // Mapper 暂时并未实现 Lifecycle 接口
        for (Mapper mapper: mappers) {
            if (mapper instanceof Lifecycle)
                ((Lifecycle) mapper).start();
        }

        for (Container child: findChildren() ){
            if (child instanceof Lifecycle) {
                ((Lifecycle) child).start();
            }
        }

        if (pipeline instanceof Lifecycle) {
            ((Lifecycle) pipeline).start();
        }

        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);

        lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
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

        ContainerEvent event = new ContainerEvent(this, type, data);

        for (ContainerListener listener: containerListeners) {
            listener.containerEvent(event);
        }
    }




    protected void addDefaultMapper(String  mapperClass) {

        if (mapperClass == null || mappers.size() >= 1) {
            return ;
        }

        try {
            Class clazz = Class.forName(mapperClass);
            Mapper mapper = (Mapper) clazz.newInstance();
            mapper.setProtocol("http");
            addMapper(mapper);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
