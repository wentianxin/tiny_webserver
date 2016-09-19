package org.apache.catalina.util;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

/**
 * Created by tisong on 8/9/16.
 */
public final class LifecycleSupport {

    private Lifecycle lifecycle;

    private LifecycleListener[] listeners = new LifecycleListener[0];


    public LifecycleSupport(Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }



    public void addLifecycleListener(LifecycleListener listener) {
        // TODO synchroinzed listeners

        LifecycleListener results[] = new LifecycleListener[listeners.length + 1];

        for (int i = 0; i < listeners.length; i++) {
            results[i] = listeners[i];
        }

        results[listeners.length] = listener;

        listeners = results;
    }

    public LifecycleListener[] findLifecycleListeners() {
        return listeners;
    }

    public void fireLifecycleEvent(String type, Object data) {

        LifecycleEvent event = new LifecycleEvent(lifecycle, type, data);

        for (LifecycleListener listener: listeners) {
            listener.lifecycleEvent(event);
        }
    }

    public void removeLifecycleListener(LifecycleListener listener) {

    }
}
