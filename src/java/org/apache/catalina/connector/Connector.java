package org.apache.catalina.connector;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.util.LifecycleMBeanBase;
import org.apache.coyote.Adapter;
import org.apache.coyote.ProtocolHandler;
import org.apache.tomcat.util.http.mapper.Mapper;

/**
 * Created by tisong on 8/21/16.
 */
public class Connector extends LifecycleMBeanBase{


    protected Adapter adapter = null;

    private ProtocolHandler protocolHandler = null;

    protected Mapper mapper = new Mapper();

    protected MapperListener mapperListener = new MapperListener(mapper, this);


    @Override
    public void initInternal() throws LifecycleException {

        super.initInternal();

        adapter = new CoyoteAdapter(this);
        protocolHandler.setAdapter(adapter);


        try {
            protocolHandler.init();  // Http11Protocol create ServerSocket object
        } catch (Exception e) {

        }

        mapperListener.init(); // just register JMX
    }
}
