package org.apache.catalina;

/**
 * Created by tisong on 8/9/16.
 */
public interface Lifecycle {

    public static final String START_EVENT = "start";

    public static final String BEFORE_START_EVENT = "before_start";

    public static final String AFTER_START_EVENT = "after_start";

    public static final String STOP_EVENT = "stop";

    public static final String BEFORE_STOP_EVENT = "before_stop";

    public static final String AFTER_STOP_EVENT = "after_stop";



    public void start() throws LifecycleException;

    public void stop() throws LifecycleException;

    public void addLifecycleListener(LifecycleListener listener);

    public LifecycleListener[] findLifecycleListener();

    public void removeLifecycleListener(LifecycleListener listener);
}
