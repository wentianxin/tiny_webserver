package org.apache.catalina.startup;

import org.apache.catalina.*;
import org.apache.catalina.core.StandardHost;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tisong on 9/9/16.
 */
public class HostConfig implements LifecycleListener{


    private Host host = null;

    protected List deployed = new ArrayList();

    protected String configClass = "org.apache.catalina.startup.ContextConfig";

    protected String contextClass = "org.apache.catalina.core.StandardContext";


    private Thread thread = null;


    private boolean threadDone = false;


    private String threadName = "HostConfig";

    @Override
    public void lifecycleEvent(LifecycleEvent event) {

        host = (Host) event.getLifecycle();

        if (host instanceof StandardHost) {
            // 设置 Host 的一些属性; 属性可以从 server.xml里面中读取, 若未写, 则使用默认属性
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
        if (!file.exists()) {
            System.out.println("tomcat/webapps 目录不存在, 请恢复源目录");
            System.exit(1);
        }

        String[] files = file.list();


        deployDescriptiors(file, files);
        deployWARS(file, files);
        deployDirectories(file, files);

    }


    private void deployDescriptiors(File appBase, String[] files) {


    }

    private void deployWARS(File appBase, String[] files) {

    }


    private void deployDirectories(File appBase, String[] files) {


        for (int i = 0; i < files.length; i++) {

            if (files[i].equalsIgnoreCase("META-INF"))
                continue;
            if (files[i].equalsIgnoreCase("WEB-INF"))
                continue;
            if (deployed.contains(files[i]))
                continue;


            File dir = new File(appBase, files[i]);
            if (dir.isDirectory()) {
                deployed.add(files[i]);

                File webInf = new File(dir, "/WEB-INF");
                if (!webInf.exists() || !webInf.isDirectory() ||
                        !webInf.canRead())
                    continue;

                String contextPath = "/" + files[i];
                if (files[i].equals("ROOT"))
                    contextPath = "";
                if (host.findChild(contextPath) != null)
                    continue;

                try {
                    URL url = new URL("file", null, dir.getCanonicalPath());
                    ((Deployer) host).install(contextPath, url);
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }



    protected File appBase() {

        File file = new File(host.getAppBase());
        if (!file.isAbsolute()) {
            file = new File(System.getProperty("catalina.base"), host.getAppBase());
        }

        return file;
    }


    public void setConfigClass(String configClass) {
        this.configClass = configClass;
    }

    public String getConfigClass() {
        return configClass;
    }


    public void setContextClass(String contextClass) {
        this.contextClass = contextClass;
    }

    public String getContextClass() {
        return contextClass;
    }
}
