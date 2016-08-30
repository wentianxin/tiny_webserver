package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.loader.WebappLoader;

/**
 * Created by tisong on 8/18/16.
 */
public class StandardContext extends ContainerBase implements Context{

    private boolean configured = false;

    private org.apache.tomcat.util.http.mapper.Mapper mapper =
            new org.apache.tomcat.util.http.mapper.Mapper();

    @Override
    protected void initInternal() throws LifecycleException {

        super.initInternal();

        if (processTlds) {
            this.addLifecycleListener(new TldConfig());
        }

    }

    @Override
    protected synchronized void startInternal() throws LifecycleException {

        setConfigured(false);

        boolean ok = true;

        if (nameingResources != null) {
            nameingResouces.start();
        }


        if (getLoader() == null ) {
            WebappLoader webappLoader = new WebappLoader(getParentClassLoader());
            webappLoader.setDelegate(getDelegate());
            setLoader(webappLaoder);
        }


        getCharsetMapper();

        postWorkDirectory();

        // Reading the "catalina.useNaming" environment variable
        String useNamingProperty = System.getProperty("catalina.useNaming");

        if (ok && isUseNaming()) {
            if (getNamingContextListener() == null) {
                // 添加 NamingContextListener 事件监听器
                NamingContextListener ncl = new NamingContextListener();
                ncl.setNaming(getNamingContextName());
                ncl.setExceptionOnFailedWrite(getJndiExceptionOnFailedWrite());
                addLifecycleListener(ncl);
                setNamingContextListener(ncl);
            }
        }


        try {

            if ( ok ) {

                if ( (loader != null) && (loader instanceof Lifecycle)) {
                    ((Lifecycle) loader).start();
                }

                if ((cluster != null) && (cluster instanceof Lifecycle))
                    ((Lifecycle) cluster).start();
                Realm realm = getRealmInternal();
                if ((realm != null) && (realm instanceof Lifecycle))
                    ((Lifecycle) realm).start();
                if ((resources != null) && (resources instanceof Lifecycle))
                    ((Lifecycle) resources).start();


                // Start our child containers, if not already started
                for (Container child : findChildren()) {
                    if (!child.getState().isAvailable()) {
                        child.start();
                    }
                }

                // Start the Valves in our pipeline (including the basic),
                // if any
                if (pipeline instanceof Lifecycle) {
                    ((Lifecycle) pipeline).start();
                }

                if ( (getCluster() != null ) && distributable ) {

                    try {
                        contextManager = getCluster().createManager(getName());
                    } catch (Exception e) {

                    }
                } else {
                    contextManager = new StandardManager();
                }

                if (contextManager != null) {
                    setManager(contextManager);
                }

                if (manager!=null && (getCluster() != null) && distributable) {
                    //let the cluster know that there is a context that is distributable
                    //and that it has its own manager
                    getCluster().registerManager(manager);
                }
            }
        } finally {

        }


        mapper.setContext(getPath(), welcomeFiles, resources);



        // Close all JARs right away to avoid always opening a peak number
        // of files on startup
        if (getLoader() instanceof WebappLoader) {
            ((WebappLoader) getLoader()).closeJARs(true);
        }

        // Reinitializing if something went wrong
        if (!ok) {
            setState(LifecycleState.FAILED);
        } else {
            setState(LifecycleState.STARTING);
        }

    }

    @Override
    public void addChild(Container child) {

        super.addChild(child);


    }

    @Override
    public void setConfigured(boolean configured) {

        boolean oldConfigured = this.configured;
        this.configured = configured;

        support.firePropertyChange("configured",
                                    oldConfigured,
                                    this.configured);
    }


}
