package org.apache.catalina.core;

import org.apache.catalina.Container;
import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;

/**
 * Created by tisong on 8/18/16.
 */
public class StandardContext extends ContainerBase implements Context{

    @Override
    protected void initInternal() throws LifecycleException {

        super.initInternal();

        if (processTlds) {
            this.addLifecycleListener(new TldConfig());
        }

    }

    @Override
    protected synchronized void startInternal() throws LifecycleException {


        setLoader(webappLaoder);

        getCharsetMapper();

        ((Lifecycle)loader).start();  // webapploader start


        fireLifecycleEvent(Lifecycle.CONFIGURE_START_EVENT, null);  // 调用的是 LifecycleBase 的 fireLifecycleEvent
    }

    @Override
    public void addChild(Container child) {

        super.addChild(child);


    }
}
