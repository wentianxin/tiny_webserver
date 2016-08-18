package org.apache.catalina.startup;

import org.apache.catalina.*;

import java.io.File;

/**
 * Created by tisong on 8/18/16.
 */
public class HostConfig implements LifecycleListener{

    private Host host = null;

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        try {
            host = (Host) event.getLifecycle();
        } catch (ClassCastException e) {

        }

        // Process the event that has occurred
        if (event.getType().equals(Lifecycle.PERIODIC_EVENT)) {
            check();
        } else if (event.getType().equals(Lifecycle.START_EVENT)) {
            start();
        } else if (event.getType().equals(Lifecycle.STOP_EVENT)) {
            stop();
        }

    }

    public void start() {


        deployApps();
    }

    /**
     * Deploy applications for any directories or WAR files that are found
     * in our "application root" directory.
     */
    protected void deployApps() {

        File appBase = appBase();
        File configBase = configBase();

        String[] filteredAppPaths = filterAppPaths(appBase.list());

        deployWARS(appBase, filteredAppPaths);

        deployDirectories(appBase, filteredAppPaths);
    }

    protected void deployWARS(File appBase, String[] files) {

    }

    protected void deployDirectories(File configBase, String[] files) {

    }

    protected void deployDirectory(ContextName cn, File dir) {


        context = (Context) digester.parse(xml);


        host.addChild(context);
    }
}
