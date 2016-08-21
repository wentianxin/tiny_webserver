package org.apache.catalina;

import java.util.concurrent.TimeUnit;

/**
 * Created by tisong on 8/21/16.
 */
public interface Executor extends java.util.concurrent.Executor, Lifecycle {

    void execute(Runnable command, long timeout, TimeUnit unit);
}
