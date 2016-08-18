package org.apache.catalina.core;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.util.LifecycleMBeanBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by tisong on 8/18/16.
 */
public abstract class ContainerBase extends LifecycleMBeanBase implements Container{

    private int startStopThreads = 1;
    private ThreadPoolExecutor startStopExecutor;


    protected HashMap<String, Container> children = new HashMap<String, Container>();


    @Override
    protected void initInternal() throws LifecycleException {


        super.initInternal();
    }

    protected synchronized void startInternal() throws LifecycleException {

        ((Lifecycle)loader).start();

        ((Lifecycle)manager).start();

        ((Lifecycle)realm).start();

        ((Lifecycle)resources).start();

        Container children[] = findChildren();
        List<Future<Void>> results = new ArrayList<>();
        for (int i = 0; i < children.length; i++) {
            results.add(startStopExecutor.submit(new StartChild(children[i])));
        }


        ((Lifecycle) pipeline).start();

        setState(LifecycleState.STARTING);
    }


    private static class StartChild implements Callable<Void> {

        private Container child;

        public StartChild(Container child) {
            this.child = child;
        }

        @Override
        public Void call() throws LifecycleException {
            child.start();
            return null;
        }
    }


    public void addChild(Container child) {

        addChildInternal(child);
    }


    private void addChildInternal(Container child) {

        child.setParent(this);

        children.put(child.getName(), child);

        try {
            child.start();
        } catch (LifecycleException e) {

        }

        fireContainerEvent(ADD_CHILD_EVENT, child);
    }
}
