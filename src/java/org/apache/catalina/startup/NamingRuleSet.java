package org.apache.catalina.startup;

import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;

/**
 * Created by tisong on 9/9/16.
 */
public class NamingRuleSet extends RuleSetBase {


    private String prefix  = null;

    public NamingRuleSet(String prefix) {
        super();
        this.namespaceURI = null;
        this.prefix = prefix;
    }



    @Override
    public void addRuleInstances(Digester digester) {


        /**
         * EJB 资源
         */
        digester.addObjectCreate(prefix + "Ejb",
                "org.apache.catalina.deploy.ContextEjb");
        digester.addSetProperties(prefix + "Ejb");
        digester.addSetNext(prefix + "Ejb",
                "addEjb",
                "org.apache.catalina.deploy.ContextEjb");



        /**
         * Environment
         */
        digester.addObjectCreate(prefix + "Environment",
                "org.apache.catalina.deploy.ContextEnvironment");
        digester.addSetProperties(prefix + "Environment");
        digester.addSetNext(prefix + "Environment",
                "addEnvironment",
                "org.apache.catalina.deploy.ContextEnvironment");



        /**
         * Local EJB
         */
        digester.addObjectCreate(prefix + "LocalEjb",
                "org.apache.catalina.deploy.ContextLocalEjb");
        digester.addSetProperties(prefix + "LocalEjb");
        digester.addSetNext(prefix + "LocalEjb",
                "addLocalEjb",
                "org.apache.catalina.deploy.ContextLocalEjb");

        /**
         * Resource 标签创建对应的对象 ContextResource
         */
        digester.addObjectCreate(prefix + "Resource",
                "org.apache.catalina.deploy.ContextResource");
        digester.addSetProperties(prefix + "Resource");
        digester.addSetNext(prefix + "Resource",
                "addResource",
                "org.apache.catalina.deploy.ContextResource");


        /**
         * 调用 NamingResources 的 addResourceEnvRef 方法
         */
        digester.addCallMethod(prefix + "ResourceEnvRef",
                "addResourceEnvRef", 2);
        digester.addCallParam(prefix + "ResourceEnvRef/name", 0);
        digester.addCallParam(prefix + "ResourceEnvRef/type", 1);



        /**
         * 根据 ResourceParams 标签 创建 ResourceParams 对象
         */
        digester.addObjectCreate(prefix + "ResourceParams",
                "org.apache.catalina.deploy.ResourceParams");
        digester.addSetProperties(prefix + "ResourceParams");
        digester.addSetNext(prefix + "ResourceParams",
                "addResourceParams",
                "org.apache.catalina.deploy.ResourceParams");


        digester.addCallMethod(prefix + "ResourceParams/parameter",
                "addParameter", 2);
        digester.addCallParam(prefix + "ResourceParams/parameter/name", 0);
        digester.addCallParam(prefix + "ResourceParams/parameter/value", 1);
    }
}
