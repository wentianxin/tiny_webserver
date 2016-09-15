package org.apache.catalina.mbeans;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

/**
 * Created by tisong on 9/15/16.
 */
public class ServerLifecycleListener implements LifecycleListener {

    public ServerLifecycleListener() {
        System.out.println("ServerLifecycleListener 实例化");
    }
    @Override
    public void lifecycleEvent(LifecycleEvent event) {

    }
}
