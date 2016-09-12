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
         * 创建 Engine 对象;
         * 增加默认监听器: EngineConfig;
         * 将 Engine 与父类 Service关联
         */
        digester.addObjectCreate(prefix + "Engine",
                                 "org.apache.catalina.core.StandardEngine",
                                 "className");
        digester.addSetProperties(prefix + "Engine");
        digester.addRule(prefix + "Engine",
                         new LifecycleListenerRule(digester,
                                                   "org.apache.catalina.core.startup.EngineConfig",
                                                    "engineConfigClass"));
        /* 调用的是Service的 setContainer方法; 将 Service 与 Engine关联起来 */
        digester.addSetNext(prefix + "Engine",
                            "setContainer",
                            "org.apache.catalina.Container");

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




    }
}
