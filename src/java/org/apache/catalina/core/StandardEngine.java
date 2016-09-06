package org.apache.catalina.core;

import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Pipeline;

/**
 * Created by tisong on 9/3/16.
 */
public class StandardEngine extends ContainerBase
    implements Engine{


    private String defaultHost = null;

    private String mapperClass = "org.apache.catalina.core.StandardEngineMapper";


    public StandardEngine() {
        super();
        pipeline.setBasic(new StandardEngineValve());
    }

    @Override
    public String getDefaultHost() {
        return this.defaultHost;
    }

    @Override
    public void setDefaultHost(String defaultHost) {
        this.defaultHost = defaultHost;
    }

    public void setMapperClass(String mapperClass) {
        this.mapperClass = mapperClass;
    }

    public String getMapperClass() {
        return mapperClass;
    }


    public void start() throws LifecycleException {

        super.start();
    }



}
