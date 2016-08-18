package org.apache.catalina.core;

import org.apache.catalina.Engine;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;

/**
 * Created by tisong on 8/18/16.
 */
public class StandardEngine extends ContainerBase implements Engine{


    @Override
    protected void initInternal() throws LifecycleException {

        super.initInternal();
    }

    @Override
    protected synchronized void startInternal() throws LifecycleException {

        super.startInternal();
    }
}
