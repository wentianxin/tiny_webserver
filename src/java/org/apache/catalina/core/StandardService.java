package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * Created by tisong on 9/6/16.
 */
public class StandardService
    implements Service, Lifecycle{

    private static final Log logger = LogFactory.getLog(StandardService.class);


    private static final StringManager sm =
            StringManager.getManager(Constants.Package);


    private Container container = null;

    private String name = null;

    private Connector[] connectors = new Connector[0];


    private boolean initiazlized = false;

    private boolean started = false;


    private LifecycleSupport lifecycleSupport = new LifecycleSupport(this);


    public StandardService() {

        logger.info("StandardService 初始化");
    }

    // --------------------------------------------- Implements Service

    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addConnector(Connector connector) {

        Connector[] results = new Connector[connectors.length + 1];

        System.arraycopy(connectors, 0, results, 0, connectors.length);

        results[connectors.length] = connector;

        connectors = results;
    }

    @Override
    public Connector[] findConnectors() {
        return this.connectors;
    }

    @Override
    public void removeConnector(Connector connector) {

        Connector[] results = new Connector[connectors.length - 1];

        int pos = 0;
        for (; pos < connectors.length; pos++) {
            if (connectors[pos] == connector) {
                break;
            }
        }

        if (pos == connectors.length) {
            return ;
        }

        if (connectors[pos] instanceof Lifecycle) {
            try {
                ((Lifecycle)connectors[pos]).stop();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0, k = 0; i < connectors.length; i++) {
            if (i == pos) {
                continue;
            }
            results[k++] = connectors[i];
        }

        connectors = results;
    }


    /**
     * Service初始化;
     * 初始化关联的 connector组件(绑定端口)
     * @throws LifecycleException
     */
    @Override
    public void initialize() throws LifecycleException {

        if (initiazlized) {
            throw new LifecycleException();
        }
        initiazlized = true;

        for (Connector connector: connectors) {
            connector.initialize();
        }
    }



    // ------------------------------------- Implements Lifecycle

    /**
     * Service启动
     * 启动关联的Connector组件; 启动绑定的Container容器
     * @throws LifecycleException
     */
    @Override
    public void start() throws LifecycleException {

        if (started) {
            throw new LifecycleException();
        }
        lifecycleSupport.fireLifecycleEvent(BEFORE_START_EVENT, null);
        lifecycleSupport.fireLifecycleEvent(START_EVENT, null);
        started = true;

        if (container != null && container instanceof Lifecycle) {
            ((Lifecycle) container).start();
        }


        /**
         * 创建线程池(队列); 并开启一个后台线程接收Socket
         */
        for (Connector connector: connectors) {
            if (connector instanceof Lifecycle) {
                ((Lifecycle) connector).start();
            }
        }

        lifecycleSupport.fireLifecycleEvent(AFTER_START_EVENT, null);
    }

    @Override
    public void stop() throws LifecycleException {

        if (!started) {
            throw new LifecycleException
                    (sm.getString("standardService.stop.notStarted"));
        }
        lifecycleSupport.fireLifecycleEvent(STOP_EVENT, null);

        started = false;

        synchronized (connectors) {
            for (int i = 0; i < connectors.length; i++) {
                if (connectors[i] instanceof Lifecycle)
                    ((Lifecycle) connectors[i]).stop();
            }
        }

        synchronized (container) {
            if ((container != null) && (container instanceof Lifecycle))
                ((Lifecycle) container).stop();
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
}
