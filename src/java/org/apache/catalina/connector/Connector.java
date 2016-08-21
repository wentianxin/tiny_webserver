package org.apache.catalina.connector;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.util.LifecycleMBeanBase;
import org.apache.coyote.Adapter;
import org.apache.coyote.ProtocolHandler;

/**
 * Created by tisong on 8/21/16.
 */
public class Connector extends LifecycleMBeanBase{


    private Adapter adapter = null;

    private ProtocolHandler protocolHandler = null;


    @Override
    public void initInternal() throws LifecycleException {

        super.initInternal();

        adapter = new CoyoteAdapter(this);
        protocolHandler.setAdapter(adapter);


        try {
            protocolHandler.init();
        } catch (Exception e) {

        }

        mapperListener.init();
    }
}
