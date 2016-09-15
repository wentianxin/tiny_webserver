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

        /**
         * Host 实例化
         */
        digester.addObjectCreate(prefix + "Host",
                                 "org.apache.catalina.core.StandardHost",
                                 "className");
        digester.addSetProperties(prefix + "Host");
        digester.addSetNext(prefix + "Host",
                "addChild",
                "org.apache.catalina.Container");


        /**
         * 增加默认的 监听器: HostConfig
         */
        digester.addRule(prefix + "Host", new LifecycleListenerRule(digester,
                                 "org.apache.catalina.startup.HostConfig",
                                 "hostConfigClass"));


        /**
         * 设置父类加载器
         */
        digester.addRule(prefix + "Host", new CopyParentClassLoaderRule(digester));


        /**
         * 监听器
         */
        digester.addObjectCreate(prefix + "Host/Listener",
                null, // MUST be specified in the element
                "className");
        digester.addSetProperties(prefix + "Host/Listener");
        digester.addSetNext(prefix + "Host/Listener",
                "addLifecycleListener",
                "org.apache.catalina.LifecycleListener");


        /**
         * 日志组件
         */
        digester.addObjectCreate(prefix + "Host/Logger",
                null, // MUST be specified in the element
                "className");
        digester.addSetProperties(prefix + "Host/Logger");
        digester.addSetNext(prefix + "Host/Logger",
                "setLogger",
                "org.apache.catalina.Logger");


        /**
         * Realm 组件
         */
        digester.addObjectCreate(prefix + "Host/Realm",
                null,
                "className");
        digester.addSetProperties(prefix + "Host/Realm");
        digester.addSetNext(prefix + "Host/Realm",
                "setRealm",
                "org.apache.catalina.Realm");


        /**
         * 流水线
         */
        digester.addObjectCreate(prefix + "Host/Valve",
                null,
                "className");
        digester.addSetProperties(prefix + "Host/Valve");
        digester.addSetNext(prefix + "Host/Valve",
                "addValve",
                "org.apache.catalina.Value");



    }
}
