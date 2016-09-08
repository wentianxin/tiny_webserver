package org.apache.catalina.startup;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleListener;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Created by tisong on 9/6/16.
 */
public class LifecycleListenerRule extends Rule{

    private String listenerClass = null;

    private String attributeName = null;


    public LifecycleListenerRule(Digester digester, String listenerClass,
                                 String attributeName) {
        super(digester);
        this.listenerClass = listenerClass;
        this.attributeName = attributeName;

    }

    /**
     * 创建 LifecycleListner 实例, 并放入 Lifecycle 实例中
     * @param attributes The attribute list of this element
     * @throws Exception
     */
    public void begin(Attributes attributes) throws Exception {
        String className = listenerClass;
        if (attributeName != null) {
            String value = attributes.getValue(attributeName);
            if (value != null) {
                className = value;
            }
        }

        Class clazz = Class.forName(className);
        LifecycleListener listener =
                (LifecycleListener) clazz.newInstance();

        Lifecycle lifecycle = (Lifecycle) digester.peek();
        lifecycle.addLifecycleListener(listener);
    }
}
