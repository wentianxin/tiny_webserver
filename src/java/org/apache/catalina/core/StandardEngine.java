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
        // Ensure that a Realm is present before any attempt is made to start
        // one. This will create the default NullRealm if necessary
        getRealm();

        super.initInternal();
    }

    @Override
    protected synchronized void startInternal() throws LifecycleException {

        super.startInternal();
    }
}
