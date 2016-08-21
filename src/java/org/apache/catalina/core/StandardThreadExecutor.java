package org.apache.catalina.core;

import org.apache.catalina.Executor;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.util.LifecycleMBeanBase;
import org.apache.tomcat.util.threads.ResizableExecutor;

/**
 * Created by tisong on 8/21/16.
 */
public class StandardThreadExecutor extends LifecycleMBeanBase implements Executor, ResizableExecutor {


    @Override
    public void initInternal() throws LifecycleException {
        super.initInternal();
    }


}
