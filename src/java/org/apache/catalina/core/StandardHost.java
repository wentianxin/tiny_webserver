package org.apache.catalina.core;


import org.apache.catalina.*;

/**
 * Created by tisong on 9/3/16.
 */
public class StandardHost
    extends ContainerBase
        implements Host {

    private String name = null;

    private String[] aliases = new String[0];

    private String appBase = ".";

    private String configClass = "";

    private String contextClass = "";

    private String errorReportValveClass = "";

    private String mapperClass = "";

    private boolean uppackWARS = true;

    private DefaultContext defaultContext = null;



    public StandardHost() {
        super();
        pipeline.setBasic(new StandardHostValve());
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

    public boolean isUnpackWARS() {
        return unpackWARS;
    }

    public void setUnpackWARs(boolean unpackWARs) {

        this.unpackWARs = unpackWARs;

    }


    public void addChild(Container child) {

        if ( !(child instanceof Context) ) {

        }

        super.addChild(child);
    }



    public void start() throws LifecycleException {

        if (errorReportValveClass != null) {

            try {
                Value value = (Value) Class.forName(errorReportValveClass).newInstance();

                addValue(value);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        // addValue(new ErrorDispatcherValve());

        super.start();
    }
}
