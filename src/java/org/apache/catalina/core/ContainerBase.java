package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

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


    protected Container parent = null;  // 可能会用到 parent loader

    protected ClassLoader parentClassLoader = null;


    protected String name = null;


    protected LifecycleSupport lifecycleSupport = new LifecycleSupport(this);


    protected boolean started = false;
    

    @Override
    public void setName(String name) {

    }

    @Override
    public String getName() {
        return null;
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

    }

    @Override
    public ClassLoader getParentClassLoader() {
        return null;
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

        if (mapper != null) {
            return mapper;
        } else {
            return mappers.get(protocol);
        }
    }

    @Override
    public Mapper[] findMappers() {

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
        return null;
    }

    @Override
    public void setBasic(Value value) {

    }

    @Override
    public Value[] getValues() {
        return new Value[0];
    }

    @Override
    public void addValue(Value value) {

    }

    @Override
    public void removeValue(Value value) {

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

    }


    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }



    // -----------------------


    public void fireContainerEvent(String type, Object data) {


    }
}
