package org.apache.catalina.connector;

import org.apache.catalina.ContainerListener;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.util.LifecycleMBeanBase;
import org.apache.tomcat.util.http.mapper.Mapper;

/**
 * Created by tisong on 8/22/16.
 */
public class MapperListener extends LifecycleMBeanBase
        implements ContainerListener, LifecycleListener{


    private Mapper mapper = null;

    private Connector connector = null;


    public MapperListener(Mapper mapper, Connector connector) {

        this.mapper = mapper;
        this.connector = connector;
    }



}
