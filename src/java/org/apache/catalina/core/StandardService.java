package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.LifecycleMBeanBase;

import java.util.ArrayList;

/**
 * Created by tisong on 8/17/16.
 */
public class StandardService extends LifecycleMBeanBase implements Service {

    /**
     * Service 是与容器的交界处
     */
    private Container container = null;


    private ArrayList<Executor> executors = new ArrayList<Executor>();

    private Connector[] connectors = new Connector[0];
    
    /**
     * 初始化容器; 初始化线程池; 初始化连接器
     * @throws LifecycleException
     */
    @Override
    public void initInternal() throws LifecycleException {
        super.initInternal();

        if (container != null) {
            container.init();
        }

        for (Executor executor: findExecutors()) {
            // TODO setDomain
            executor.init();
        }

        // TODO synchronized
        for (Connector connector: connectors) {
            connector.init();
        }
    }

    @Override
    protected void startInternal() throws LifecycleException{

        container.start();

        for (Executor executor: executors) {
            executor.start();
        }

        for (Connector connector: coonectors) {
            connector.start();
        }
    }
}
