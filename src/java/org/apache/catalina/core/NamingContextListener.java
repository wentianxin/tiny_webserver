package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.deploy.ContextResource;
import org.apache.catalina.deploy.ContextResourceLink;
import org.apache.catalina.deploy.NamingResources;
import org.apache.naming.NamingContext;
import org.apache.naming.ResourceRef;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by tisong on 9/7/16.
 */
public class NamingContextListener implements LifecycleListener{

    private Object container = null;

    private String name = "/";

    private NamingResources namingResources = null;

    private NamingContext   namingContext = null;

    private javax.naming.Context compCtx = null;

    private javax.naming.Context envCtx  = null;



    // ----------------------------------------------------- Implements LifecycleListener

    @Override
    public void lifecycleEvent(LifecycleEvent event) {

        container = event.getLifecycle();

        if (container instanceof Context) {
            namingResources = ((Context)container).getNamingResources();
        } else if (container instanceof Server) {
            namingResources = ((Server) container).getGlobalNamingResources();
        } else {
            return ;
        }

        if (event.getType() == Lifecycle.START_EVENT) {

            System.out.println("NamingContextListener START_EVENT 事件触发");
            //HashMap contextEnv = new HashMap();
            //生成这个StandardContext域的JNDI对象树根NamingContext对象
            //namingContext = new NamingContext(contextEnv, getName());

            //将此StandardContext对象与JNDI对象树根NamingContext对象绑定
            //ContextBindings.bindContext(container, namingContext, container);

            //将初始化时的资源对象绑定JNDI对象树
            //createNamingContext();
        } else if (event.getType() == Lifecycle.STOP_EVENT) {
            System.out.println("NamingContextListener STOP_EVENT 事件触发");
        }
    }


    private void createNamingContext() throws NamingException{

        if (container instanceof Server) {

        } else {
            // 创建根节点
            compCtx = namingContext.createSubcontext("comp");
            envCtx = compCtx.createSubcontext("env");
        }

        ContextResource[] resources = namingResources.findResources();
        for (ContextResource cr: resources) {
            addResource(cr);
        }
    }


    public void addResource(ContextResource resource) {
        // 创建要绑定的叶子资源
        Reference ref = new ResourceRef(resource.getType(), resource.getDescription(),
                resource.getScope(),resource.getAuth());

        try {
            // 创建中间的枝干
            createSubcontexts(envCtx, resource.getName());
            // 绑定枝干下最后的叶子: 资源
            envCtx.bind(resource.getName(), ref);
        } catch (NamingException e) {

        }
    }


    private void createSubcontexts(javax.naming.Context context, String name) throws NamingException{

        javax.naming.Context currentContext = context;
        // TODO 关于 StringTokenizer 的使用
        StringTokenizer tokenizer = new StringTokenizer(name, "/");

        //tokenizer.hasMoreElements();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!token.equals("") && tokenizer.hasMoreTokens()) {

                try {
                    currentContext = context.createSubcontext(token);
                } catch (NamingException e) {
                    // 可鞥已经创建过该枝干(NamingContext); 所以查找是否已经创建过;
                    currentContext =
                            (javax.naming.Context) currentContext.lookup(token);
                }
            }
        }

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
