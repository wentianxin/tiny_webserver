package org.apache.catalina.util;

import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.tomcat.util.modeler.Registry;

import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Created by tisong on 8/18/16.
 */
public abstract class LifecycleMBeanBase extends LifecycleBase implements MBeanRegistration {


    private String domain = null;

    private ObjectName oname = null;

    private MBeanServer mserver = null;


    @Override
    protected void initInternal() throws LifecycleException {

        if (oname == null) {
            mserver = Registry.getRegistry(null, null).getMBeanServer();

            oname = register(this, getObjectNameKeyProperties());
        }
    }


    @Override
    protected void startInternal() throws LifecycleException {

    }

    protected final ObjectName register(Object obj,
            String objectNameKeyProperties) {

        StringBuilder name = new StringBuilder(getDomain());

        ObjectName on = null;

        try {
            on = new ObjectName(name.toString());

            Registry.getRegistry(null, null).registerComponent(obj, on, null);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (Exception e) {

        }

        return on;
    }



    private String getDomain() {
        if (domain == null) {
            domain = getDomainInternal();
        }

        if (domain == null) {
            domain = Globals.DEFAULT_MBEAN_DOMAIN;
        }

        return domain;
    }

    protected abstract String getObjectNameKeyProperties();
}
