//package org.tisong.proj4.core;
//
//import org.apache.catalina.Lifecycle;
//import org.apache.catalina.LifecycleException;
//import org.apache.catalina.LifecycleListener;
//import org.apache.catalina.Loader;
//
///**
// * Created by tisong on 8/9/16.
// */
//
//public class SimpleLoader implements Loader, Lifecycle{
//
//    private ClassLoader classLoader;
//
//
//    public SimpleLoader() {
//
//
//    }
//
//    // ------------------------------------------------------- Implements Loader
//
//    @Override
//    public ClassLoader getClassLoader() {
//        return null;
//    }
//
//
//    // ------------------------------------------------------- Implements Lifecycle
//
//
//    @Override
//    public void start() throws LifecycleException {
//        System.out.println("Starting SimpleLoader");
//    }
//
//    @Override
//    public void stop() throws LifecycleException {
//
//    }
//
//    @Override
//    public void addLifecycleListener(LifecycleListener listener) {
//
//    }
//
//    @Override
//    public LifecycleListener[] findLifecycleListener() {
//        return new LifecycleListener[0];
//    }
//
//    @Override
//    public void removeLifecycleListener(LifecycleListener listener) {
//
//    }
//
//
//}
