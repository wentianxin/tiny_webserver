package org.apache.catalina.core;


import org.apache.catalina.*;
import org.apache.catalina.valves.ErrorDispatcherValve;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.io.IOException;
import java.net.URL;

/**
 * Created by tisong on 9/3/16.
 */
public class StandardHost
    extends ContainerBase
        implements Host, Deployer{

    private static final Log logger = LogFactory.getLog(StandardHost.class);

    private String[] aliases = new String[0];

    private String appBase = ".";

    private String configClass = "org.apache.catalina.core.ContextConfig";

    private String contextClass = "org.apache.catalina.core.StandardContext";

    private String errorReportValveClass = "org.apache.catalina.valves.ErrorReportValve";

    private String mapperClass = "org.apache.catalina.core.StandardHostMapper";

    private boolean unpackWARS = true;

    private boolean autoDeploy = true;

    private DefaultContext defaultContext = null;

    private Deployer deployer = new StandardHostDeployer(this);



    public StandardHost() {
        super();
        pipeline.setBasic(new StandardHostValve());

        logger.info("StandardHost 实例化");
    }


    @Override
    public String getAppBase() {
        return this.appBase;
    }

    @Override
    public void setAppBase(String appBase) {
        this.appBase = appBase;
    }



    @Override
    public Context map(String uri) {

        if (uri == null) {
            return null;
        }

        Context context = null;

        String mapUri = uri;

        while (true) {
            context = (Context) findChild(mapUri);
            if (context != null) { break;}

            int slash = mapUri.lastIndexOf('/');
            if (slash < 0) { break; }

            mapUri = mapUri.substring(0, slash);
        }

        if (context == null) {
            // Default Context
            context = (Context) findChild("");
        }

        return context;
    }



    @Override
    public String[] findAliases() {
        return this.aliases;
    }

    public void addAlias(String alias) {

    }

    @Override
    public void removeAlias(String alias) {

    }

    @Override
    public void importDefaultContext(Context context) {

    }

    public boolean isUnpackWARS() {
        return unpackWARS;
    }

    public void setUnpackWARs(boolean unpackWARs) {
        this.unpackWARS = unpackWARs;
    }

    public void setAutoDeploy(boolean autoDeploy) {
        this.autoDeploy = autoDeploy;
    }
    public boolean getAutoDeploy() {
        return this.autoDeploy;
    }


    public void addChild(Container child) {

        if ( !(child instanceof Context) ) {

        }

        super.addChild(child);
    }


    /**
     * Host 容器启动: 增加默认 Valve <code>ErrorReportValve</code>; <code>ErrorDispatcherValve</code>
     */
    public void start() throws LifecycleException {

        /**
         * 增加 Error Report  Valve
         */
        if (errorReportValveClass != null) {

            try {
                // TODO 就算我 没有运行该方法,
                System.out.println("StandardHost start");
//                Class.forName("test").newInstance();
                Value value = (Value) Class.forName(errorReportValveClass).newInstance();

                addValue(value);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        /**
         * 增加 Error Disptacher Valve
         */
        addValue(new ErrorDispatcherValve());

        super.start();
    }


    public String getContextClass() {
        return contextClass;
    }

    public void setContextClass(String contextClass) {
        this.contextClass = contextClass;
    }

    public String getConfigClass() {
        return configClass;
    }

    public void setConfigClass(String configClass) {
        this.configClass = configClass;
    }







    // --------------------------------------------------- implements Deployer

    @Override
    public void install(String contextPath, URL war) {

        deployer.install(contextPath, war);
    }

    @Override
    public void install(URL config, URL war) throws IOException {
        deployer.install(config, war);
    }

    @Override
    public Context findDepolyedApp(String contextPath) {
        return deployer.findDepolyedApp(contextPath);
    }

    @Override
    public String[] findDepolyedApps() {
        return deployer.findDepolyedApps();
    }

    @Override
    public void remove(String contextPath) throws IOException {
        deployer.remove(contextPath);
    }

    @Override
    public void start(String contextPath) throws IOException {
        deployer.start(contextPath);
    }

    @Override
    public void stop(String contextPath) throws IOException {
        deployer.stop(contextPath);
    }



    // -------------------------------------------------------- Protected Methods

    protected void addDefaultMapper(String mapperClass) {
        super.addDefaultMapper(this.mapperClass);
    }
}
