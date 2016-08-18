package org.apache.catalina.startup;

import org.apache.catalina.Engine;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;

/**
 * Created by tisong on 8/18/16.
 */
public class EngineConfig implements LifecycleListener {


    private Engine engine = null;


    @Override
    public void lifecycleEvent(LifecycleEvent event) {

        try {
            engine = (Engine) event.getLifecycle(); // 获取事件源
        } catch (ClassCastException e) {

        }

        if (event.getType().equals(Lifecycle.START_EVENT)) {
            start();
        } else if (event.getType().equals(Lifecycle.STOP_EVENT)) {
            stop();
        }
    }

    protected void start() {
        // 不做处理，只是用日志记录
    }

    protected void stop() {

    }
}
