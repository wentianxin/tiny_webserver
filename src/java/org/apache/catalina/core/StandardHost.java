package org.apache.catalina.core;


import org.apache.catalina.*;
import org.apache.catalina.valves.ErrorDispatcherValve;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * Created by tisong on 9/3/16.
 */
public class StandardHost
    extends ContainerBase
        implements Host {

    private static final Log logger = LogFactory.getLog(StandardHost.class);

    private String name = null;

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

//    @Override
//    public void setName() {
//
//    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Context map(String uri) {
        return null;
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
}
