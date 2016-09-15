package org.apache.catalina.startup;

import org.apache.catalina.Container;
import org.apache.catalina.Loader;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.Rule;
import org.apache.tomcat.util.digester.RuleSetBase;
import org.xml.sax.Attributes;

import java.lang.reflect.Constructor;

/**
 * Created by tisong on 9/6/16.
 */
public class ContextRuleSet extends RuleSetBase{

    private String prefix = null;

    public ContextRuleSet(String prefix) {

        super();
        this.namespaceURI = null;
        this.prefix = prefix;
    }



    @Override
    public void addRuleInstances(Digester digester) {

        /**
         * StandardContext 实例化
         */
        digester.addObjectCreate(prefix + "Context",
                                 "org.apache.catalina.core.StandardContext",
                                 "className");
        digester.addSetProperties(prefix + "Context");
        digester.addSetNext(prefix = "Context",
                "addChild",
                "org.apache.catalina.Container");

        /**
         * StandardContext 默认监听器: ContextConfig
         */
        digester.addRule(prefix + "Context", new LifecycleListenerRule(digester,
                                 "org.apache.catalina.startup.ContextConfig",
                                 "contextConfigClass"));


        digester.addRule(prefix + "Context", new CopyParentClassLoaderRule(
                digester));


        /**
         * 监听器
         */
        digester.addObjectCreate(prefix + "Context/Listener",
                null,
                "className");
        digester.addSetProperties(prefix + "Context/Listener");
        digester.addSetNext(prefix + "Context/Listener",
                "addLifecycleListener",
                "org.apache.catalina.LifecycleListener");


        /**
         * Loader 关联
         * 因为 Loader 构造器的实例化要传入参数, 所以不能使用 class.newInstance();
         * 而是使用 Constructor 实例化
         */
        digester.addRule(prefix + "Context/Loader", new CreateLoaderRule(digester,
                                                        "org.apache.catalina.loader.WebappLoader",
                                                        "className"));
        digester.addSetProperties(prefix + "Context/Loader");
        digester.addSetNext(prefix + "Context/Loader",
                        "setLoader",
                        "org.apache.catalina.Loader");



//        digester.addObjectCreate(prefix + "Context/Logger",
//                null,
//                "className");
//        digester.addSetProperties(prefix + "Context/Logger");
//        digester.addSetNext(prefix + "Context/Logger",
//                "setLogger",
//                "org.apache.catalina.Logger");


        /**
         * Manager 组件
         */
//        digester.addObjectCreate(prefix + "Context/Manager",
//                "org.apache.catalina.session.StandardManager",
//                "className");
//        digester.addSetProperties(prefix + "Context/Manager");
//        digester.addSetNext(prefix + "Context/Manager",
//                "setManager",
//                "org.apache.catalina.Manager");


//        digester.addObjectCreate(prefix + "Context/Manager/Store",
//                null, // MUST be specified in the element
//                "className");
//        digester.addSetProperties(prefix + "Context/Manager/Store");
//        digester.addSetNext(prefix + "Context/Manager/Store",
//                "setStore",
//                "org.apache.catalina.Store");

        // TODO ApplicationParameter

        digester.addObjectCreate(prefix + "Context/Parameter",
                                "org.apache.catalina.deploy.ApplicationParameter",
                                "className");
        digester.addSetProperties(prefix + "Context/Parameter");
        digester.addSetNext(prefix + "Context/Parameter",
                            "addApplicationParameter",
                            "org.apache.catalina.deploy.ApplicationParameter");


        // TODO Relam

        digester.addObjectCreate(prefix + "Context/Realm",
                null, // MUST be specified in the element
                "className");
        digester.addSetProperties(prefix + "Context/Realm");
        digester.addSetNext(prefix + "Context/Realm",
                "setRealm",
                "org.apache.catalina.Realm");


        // TODO ResourceLink - ContextResourceLink
        digester.addObjectCreate(prefix + "Context/ResourceLink",
                "org.apache.catalina.deploy.ContextResourceLink");
        digester.addSetProperties(prefix + "Context/ResourceLink");
        digester.addSetNext(prefix + "Context/ResourceLink",
                "addResourceLink",
                "org.apache.catalina.deploy.ContextResourceLink");

        // TODO Resources - FileDirContext
        digester.addObjectCreate(prefix + "Context/Resources",
                "org.apache.naming.resources.FileDirContext",
                "className");
        digester.addSetProperties(prefix + "Context/Resources");
        digester.addSetNext(prefix + "Context/Resources",
                "setResources",
                "javax.naming.directory.DirContext");


        digester.addObjectCreate(prefix + "Context/Valve",
                null,
                "className");
        digester.addSetProperties(prefix + "Context/Valve");
        digester.addSetNext(prefix + "Context/Valve",
                "addValue",
                "org.apache.catalina.Value");


        //TODO call addWrapperLifecycle
        //TODO call addWrapperListener

    }
}


final class CreateLoaderRule extends Rule {

    private String attributeName;

    private String loaderClass;


    public CreateLoaderRule(Digester digester, String loaderClass,
                            String attributeName) {

        super(digester);
        this.loaderClass = loaderClass;       // WebappClassLoader
        this.attributeName = attributeName;   // className
    }


    /**
     * 根据 loaderClass 创建 Loader(构建的时候要传递参数; 而 ClassLoader.loadClass 以及 class.newInstance 并没有参数)对象
     * @param attributes The attribute list of this element
     * @throws Exception
     */
    public void begin(Attributes attributes) throws Exception {

        Container container = (Container) digester.peek();
        ClassLoader parentClassLoader = container.getParentClassLoader();

        String className = loaderClass;

        Class clazz = Class.forName(className);

        Class [] types  =  { ClassLoader.class };
        Object[] args   =  { parentClassLoader };

        Constructor<Loader> constructor = clazz.getDeclaredConstructor(types);

        Loader loader = constructor.newInstance(args);

        digester.push(loader);

    }


    public void end() throws Exception {

        digester.pop();
    }

}
