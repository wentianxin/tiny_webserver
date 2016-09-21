package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.startup.ContextRuleSet;
import org.apache.catalina.startup.NamingRuleSet;
import org.apache.catalina.util.StringManager;
import org.apache.tomcat.util.digester.Digester;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by tisong on 9/9/16.
 */
public class StandardHostDeployer implements Deployer{


    protected static StringManager sm =
            StringManager.getManager(Constants.Package);


    private StandardHost host = null;

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


        System.out.println("Host 部署Context应用: contextPath: " + contextPath);

        /**
         * 利用反射来加载 Context 实例变量
         * 并触发相关监听器
         */

        String url = war.toString();
        String docBase = null;
        if (url.startsWith("jar:")) {
            url = url.substring(4, url.length() - 2);
        }
        if (url.startsWith("file://"))
            docBase = url.substring(7);
        else if (url.startsWith("file:"))
            docBase = url.substring(5);
        else
            throw new IllegalArgumentException
                    (sm.getString("standardHost.warURL", url));

        try {
            Class clazz = Class.forName(host.getContextClass());

            Context context = (Context)clazz.newInstance();

            /**
             * 这里区分了 contextPath 与 docBase 两个路径
             */
            context.setPath(contextPath);
            context.setDocBase(docBase);


            if (context instanceof Lifecycle) {
                clazz = Class.forName(host.getConfigClass());
                LifecycleListener listener =
                        (LifecycleListener) clazz.newInstance();
                ((Lifecycle) context).addLifecycleListener(listener);
            }


            host.fireContainerEvent(PRE_INSTALL_EVENT, context);
            host.addChild(context);
            host.fireContainerEvent(INSTALL_EVENT, context);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


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
