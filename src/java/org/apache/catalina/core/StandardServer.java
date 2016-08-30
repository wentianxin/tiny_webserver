package org.apache.catalina.core;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.deploy.NamingResources;
import org.apache.catalina.util.LifecycleMBeanBase;

/**
 * Created by tisong on 8/17/16.
 */
public class StandardServer extends LifecycleMBeanBase implements Server {
    private Service services[] = new Service[0];

    private NamingResources globalNamingResources = null;


    @Override
    public void initInternal() throws LifecycleException {

        super.initInternal();


        // TODO
        // Register global String cache
        // Note although the cache is global, if there are multiple Servers
        // present in the JVM (may happen when embedding) then the same cache
        // will be registered under multiple names
        // onameStringCache = register(new StringCache(), "type=StringCache");


        globalNamingResources.init();


        for (int i = 0; i < services.length; i++) {
            services[i].init();
        }

    }


    @Override
    protected void startInternal() throws LifecycleException {

        // TODO
        fireLifecycleEvent(CONFIGURE_START_EVENT, null);

        setState(LifecycleState.STARTING);


        globalNamingResources.start();


        for (int i = 0; i < services.length; i++) {
            services[i].start();
        }
    }

    @Override
    protected final String getObjectNameKeyProperties() {
        return "type=Server";
    }


}
