package org.apache.catalina.startup;

import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;

/**
 * Created by tisong on 9/6/16.
 */
public class EngineRuleSet extends RuleSetBase{

    private String prefix = null;


    public EngineRuleSet(String prefix) {

        super();
        this.namespaceURI = null;
        this.prefix = prefix;
    }

    @Override
    public void addRuleInstances(Digester digester) {

        /**
         * Engine 实例化
         */
        digester.addObjectCreate(prefix + "Engine",
                                 "org.apache.catalina.core.StandardEngine",
                                 "className");
        digester.addSetProperties(prefix + "Engine");
        digester.addSetNext(prefix + "Engine",
                            "setContainer",
                            "org.apache.catalina.Container");

        digester.addRule(prefix + "Engine", new LifecycleListenerRule(digester,
                        "org.apache.catalina.startup.EngineConfig",
                        "engineConfigClass"));


        /**
         * 增加 Listener 规则
         */
        digester.addObjectCreate(prefix + "Engine/Listener",
                                 null,
                                 "className");
        digester.addSetProperties(prefix + "Engine/Listener");
        digester.addSetNext(prefix + "Engine/Listener",
                            "addLifecycleListener",
                            "org.apache.catalina.LifecycleListener");


        /**
         * 增加 Loader 规则
         */
        digester.addObjectCreate(prefix + "Engine/Logger",
                                "org.apache.catalina.Logger",
                                "className");
        digester.addSetProperties(prefix + "Engine/Logger");
        digester.addSetNext(prefix + "Engine/Logger",
                            "setLogger",
                            "org.apache.catalina.Logger");

        /**
         * 增加 Realm 规则
         */
        digester.addObjectCreate(prefix + "Engine/Realm",
                                "org.apache.catalina.Realm",
                                "className");
        digester.addSetProperties(prefix + "Engine/Realm");
        digester.addSetNext(prefix + "Engine/Realm",
                            "setRealm",
                            "org.apache.catalina.Realm");


        /**
         * 流水线
         */
//        digester.addObjectCreate(prefix + "Valve",
//                                "org.apache.catalina.Value",
//                                "className");
//        digester.addSetProperties(prefix + "Valve");
//        digester.addSetNext(prefix + "Valve",
//                            "add");
    }
}
