package org.apache.catalina.core;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.util.LifecycleMBeanBase;

/**
 * Created by tisong on 8/17/16.
 */
public class StandardServer extends LifecycleMBeanBase implements Server {
    private Service services[] = new Service[0];


    @Override
    public void initInternal() throws LifecycleException {
        for (int i = 0; i < services.length; i++) {
            services[i].init();
        }

    }


    @Override
    protected void startInternal() throws LifecycleException {

        for (int i = 0; i < services.length; i++) {
            services[i].start();
        }
    }


}
