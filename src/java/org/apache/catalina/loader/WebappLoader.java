package org.apache.catalina.loader;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.Loader;

/**
 * Created by tisong on 9/15/16.
 */
public class WebappLoader implements Loader, Lifecycle{


    private ClassLoader parentClassLoader = null;

    public WebappLoader(ClassLoader classLoader) {

        this.parentClassLoader = classLoader;
    }

    @Override
    public ClassLoader getClassLoader() {
        return null;
    }




    @Override
    public void start() throws LifecycleException {

    }

    @Override
    public void stop() throws LifecycleException {

    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }
}
