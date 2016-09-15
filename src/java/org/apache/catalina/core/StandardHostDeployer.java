package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.startup.ContextRuleSet;
import org.apache.catalina.startup.NamingRuleSet;
import org.apache.tomcat.util.digester.Digester;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by tisong on 9/9/16.
 */
public class StandardHostDeployer implements Deployer{

    private Host host = null;

    private Context context = null;



    private Digester digester = null;

    private ContextRuleSet contextRuleSet = null;

    private NamingRuleSet  namingRuleSet  = null;


    public StandardHostDeployer(StandardHost host) {

        this.host = host;
    }


    @Override
    public String getName() {
        return null;
    }

    public void install(String contextPath, URL war) {

        /**
         * 利用反射来加载 Context 实例变量
         * 并触发相关监听器
         */

//        Class clazz = Class.forName(host.getContextClass());
//
//        try {
//            Context context = (Context)clazz.newInstance();
//
//            context.setPath();
//            context.setDocBase();
//
//            clazz = Class.forName(host.getConfigClass()); // 获取关联的 Context Config Class
//            LifecycleListener listener = (LifecycleListener)clazz.newInstance();
//            ((Lifecycle)context).addLifecycleListener(listener);
//
//            host.fireContainerEvent(PRE_INSTALL_EVENT, context);
//            host.addChild(context);
//            host.fireContainerEvent(INSTALL_EVENT, context);
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

    }


    public synchronized void install(URL config, URL war) throws IOException {

        InputStream stream = null;

        try {

            stream = config.openStream();
            Digester digester = createDigester();
            digester.push(this);
            digester.parse(stream);
            stream.close();
            stream = null;
        } catch (Exception e) {
            throw new IOException();
        }
    }

    @Override
    public Context findDepolyedApp(String contextPath) {
        return null;
    }

    @Override
    public String[] findDepolyedApps() {
        return new String[0];
    }

    @Override
    public void remove(String contextPath) throws IOException {

    }

    @Override
    public void start(String contextPath) throws IOException {

    }

    @Override
    public void stop(String contextPath) throws IOException {

    }


    protected Digester createDigester() {

        if (digester == null) {

            digester = new Digester();
            digester.setValidating(false);

            contextRuleSet = new ContextRuleSet("");

            digester.addRuleSet(contextRuleSet);

            namingRuleSet = new NamingRuleSet("Context/");

            digester.addRuleSet(namingRuleSet);
        }

        return digester;
    }
}
