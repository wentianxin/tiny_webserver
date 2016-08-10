package org.apache.catalina.util;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleListener;

/**
 * Created by tisong on 8/9/16.
 */
public final class LifecycleSupport {

    private Lifecycle lifecycle;

    private LifecycleListener[] listeners = new LifecycleListener[0];


    public void addLifecycleListener(LifecycleListener listener) {
        // TODO synchroinzed listeners

    }

    public LifecycleListener[] findLifecycleListeners() {
        return listeners;
    }

    public void fireLifecycleEvent(String type, Object data) {

    }

    public void removeLifecycleListener(LifecycleListener listener) {

    }
}
