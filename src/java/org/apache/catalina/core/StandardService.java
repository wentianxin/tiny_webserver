package org.apache.catalina.core;

import org.apache.catalina.Connector;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Service;
import org.apache.catalina.util.LifecycleMBeanBase;

import java.util.concurrent.Executor;

/**
 * Created by tisong on 8/17/16.
 */
public class StandardService extends LifecycleMBeanBase implements Service {


    @Override
    public void initInternal() throws LifecycleException {
        super.initInternal();

        container.init();

        executor.init();

        connector.init();
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
