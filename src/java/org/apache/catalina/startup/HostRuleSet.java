package org.apache.catalina.startup;

import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;

/**
 * Created by tisong on 9/6/16.
 */
public class HostRuleSet extends RuleSetBase{

    private String prefix = null;  // Server/Service/Engine/

    public HostRuleSet(String prefix) {
        super();
        this.namespaceURI = null;
        this.prefix = prefix;
    }

    @Override
    public void addRuleInstances(Digester digester) {


        digester.addObjectCreate(prefix + "Host",
                                 "org.apache.catalina.core.StandardHost",
                                 "className");
        digester.addSetProperties(prefix + "Host");
        // TODO copy parent laoder
        digester.addRule(prefix + "Host",
                         new LifecycleListenerRule(digester,
                                 "org.apache.catalina.startup.HostConfig",
                                 "hostConfigClass"));
        digester.addSetNext(prefix + "Host",
                            "addChild",
                            "org.apache.catalina.Container");


        digester.addObjectCreate(prefix + "Host/Listener",
                null, // MUST be specified in the element
                "className");
        digester.addSetProperties(prefix + "Host/Listener");
        digester.addSetNext(prefix + "Host/Listener",
                "addLifecycleListener",
                "org.apache.catalina.LifecycleListener");


        digester.addObjectCreate(prefix + "Host/Logger",
                null, // MUST be specified in the element
                "className");
        digester.addSetProperties(prefix + "Host/Logger");
        digester.addSetNext(prefix + "Host/Logger",
                "setLogger",
                "org.apache.catalina.Logger");




        digester.addObjectCreate(prefix + "Host/Realm",
                null, // MUST be specified in the element
                "className");
        digester.addSetProperties(prefix + "Host/Realm");
        digester.addSetNext(prefix + "Host/Realm",
                "setRealm",
                "org.apache.catalina.Realm");


        digester.addObjectCreate(prefix + "Host/Valve",
                null, // MUST be specified in the element
                "className");
        digester.addSetProperties(prefix + "Host/Valve");
        digester.addSetNext(prefix + "Host/Valve",
                "addValve",
                "org.apache.catalina.Valve");



    }
}
