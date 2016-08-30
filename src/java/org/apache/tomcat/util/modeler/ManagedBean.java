package org.apache.tomcat.util.modeler;

import javax.management.DynamicMBean;
import javax.management.InstanceNotFoundException;

/**
 * Created by tisong on 8/21/16.
 */
public class ManagedBean implements java.io.Serializable{


    public DynamicMBean createMBean(Object instance)
        throws InstanceNotFoundException {

    }
}
