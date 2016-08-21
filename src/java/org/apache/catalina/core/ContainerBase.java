package org.apache.catalina.core;

import org.apache.catalina.Container;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.util.LifecycleMBeanBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tisong on 8/18/16.
 */
public abstract class ContainerBase extends LifecycleMBeanBase implements Container{

    /**
     * 一个容器处理start 和 stop事件的可用线程数
     * 1. startStopThreads > 0 : startStopThreads
     * 2. startStopThreads = 0 : JVM 可用线程数
     * 3. startStopThreads < 0 : JVM 可用线程数 + startStopThreads
     */
    private int startStopThreads = 1;
    private ThreadPoolExecutor startStopExecutor;


    protected HashMap<String, Container> children = new HashMap<String, Container>();


    @Override
    protected void initInternal() throws LifecycleException {

        BlockingQueue<Runnable> startStopQueue =
                new LinkedBlockingQueue<Runnable>();

        startStopExecutor = new ThreadPoolExecutor(
                getStartStopThreadsInternal(),
                getStartStopThreadsInternal(), 10, TimeUnit.SECONDS,
                startStopQueue,
                new StartStopThreadFactory(getName() + "-startStop-"));

        startStopExecutor.allowCoreThreadTimeOut(true);

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

    @Override
    public int getStartStopThreads() {
        return startStopThreads;
    }

    private int getStartStopThreadsInternal() {

        int result = getStartStopThreads();
        if (result > 0) {
            return result;
        }

        result = Runtime.getRuntime().availableProcessors() + result;
        if (result < 1) {
            return 1;
        }

        return result;

    }

    private static class StartStopThreadFactory implements ThreadFactory {

        private final ThreadGroup group;

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final String namePrefix;

        public StartStopThreadFactory(String namePrefix) {
            SecurityManager s = System.getSecurityManager();

            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

            this.namePrefix = namePrefix;
        }
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }
}
