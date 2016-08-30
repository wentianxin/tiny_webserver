package org.apache.tomcat.util.modeler;

import javax.management.DynamicMBean;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * Created by tisong on 8/21/16.
 */
public class Registry implements RegisterMBean, MBeanRegistration{

    private static Registry registry = null;

    private MBeanServer server = null;

    public static synchronized Registry getRegistry(Object key, Object guard) {


    }


    public void registerComponent(Object bean, ObjectName oname, String type)
        throws Exception {

        ManagedBean
        DynamicMBean mbean = managed

        getMBeanServer().registerMBean(mbean, oname);
    }

    // -------------------------------------------------------- Interface MBeanRegistration

    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        return null;
    }

    @Override
    public void postRegister(Boolean registrationDone) {

    }

    @Override
    public void preDeregister() throws Exception {

    }

    @Override
    public void postDeregister() {

    }


    public MBeanServer getMBeanServer() {
        return server;
    }
}
