package org.apache.catalina.startup;

import org.apache.catalina.Host;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.core.StandardHost;

import java.io.File;

/**
 * Created by tisong on 9/9/16.
 */
public class HostConfig implements LifecycleListener{


    private Host host = null;

    @Override
    public void lifecycleEvent(LifecycleEvent event) {

        Host host = (Host) event.getLifecycle();

        if (host instanceof StandardHost) {

        }

        if (event.getType().equals(Lifecycle.START_EVENT)) {
            start();
        } else if (event.getType().equals(Lifecycle.STOP_EVENT)) {
            stop();
        }
    }

    private void start() {

        deployApps();
    }


    private void stop() {

    }

    protected void deployApps() {

        File file = appBase();

        String[] files = file.list();



    }


    private void depolyDescriptiors(File appBase, String[] files) {


    }

    private void depolyWARS(File appBase, String[] files) {

    }


    private void depolyDirectories(File appBase, String[] files) {


    }



    protected File appBase() {

        File file = new File(host.getAppBase());
        if (!file.isAbsolute()) {
            file = new File(System.getProperty("catalina.base"), host.getAppBase());
        }

        return file;
    }


}
