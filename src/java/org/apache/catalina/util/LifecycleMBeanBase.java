package org.apache.catalina.util;

import org.apache.catalina.LifecycleException;

import javax.management.MBeanRegistration;

/**
 * Created by tisong on 8/18/16.
 */
public abstract class LifecycleMBeanBase extends LifecycleBase implements MBeanRegistration {

    @Override
    protected void initInternal() throws LifecycleException {

    }


    @Override
    protected void startInternal() throws LifecycleException {

    }
}
