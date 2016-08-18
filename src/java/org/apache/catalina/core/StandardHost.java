package org.apache.catalina.core;

import com.sun.org.glassfish.external.probe.provider.annotations.Probe;
import org.apache.catalina.Container;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;

/**
 * Created by tisong on 8/18/16.
 */
public class StandardHost extends ContainerBase implements Host{



    @Override
    protected synchronized void startInternal() throws LifecycleException {


        super.startInternal();
    }

    @Override
    public void addChild(Container child) {

        child.addLifecycleListener(new MemoryLeakTrackingListener());


        super.addChild(child);
    }
}
