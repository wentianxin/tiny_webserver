package org.apache.catalina.deploy;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by tisong on 9/7/16.
 */
public class NamingResources {

    private static final Log logger = LogFactory.getLog(NamingResources.class);


    /**
     * 关联绑定的容器
     */
    private Object container = null;

    /**
     * 对应的 Resource 标签
     */
    private HashMap resources = new HashMap();

    /**
     *
     */
    private HashMap resourcesLink = new HashMap();

    /**
     * 对应的 ResourceParam 标签
     */
    private HashMap resourcesParams = new HashMap();


    /**
     * 对应的 Environment 标签
     */
    private HashMap envs = new HashMap();


    /**
     * name - type 键值对
     */
    private Hashtable entires = new Hashtable();



    public NamingResources() {

        logger.info("NamingResources 实例化");
    }

    public Object getContainer() {
        return container;
    }

    public void setContainer(Object container) {
        this.container = container;
    }

    public HashMap getResources() {
        return resources;
    }

    public void setResources(HashMap resources) {
        this.resources = resources;
    }




    public HashMap getResourcesLink() {
        return resourcesLink;
    }

    public void setResourcesLink(HashMap resourcesLink) {
        this.resourcesLink = resourcesLink;
    }

    public HashMap getResourcesParams() {
        return resourcesParams;
    }

    public void setResourcesParams(HashMap resourcesParams) {
        this.resourcesParams = resourcesParams;
    }




    public HashMap getEnvs() {
        return envs;
    }

    public void setEnvs(HashMap envs) {
        this.envs = envs;
    }



    public ContextResource[] findResources() {
        // TODO 返回 HashMap resources
        return null;
    }


    // -------------------------------------------- Add Method


    public void addResource(ContextResource resource) {

        entires.put(resource.getName(), resource.getType());


        resource.setResources(this);

        resources.put(resource.getName(), resource);


    }

    public void addEnvironment(ContextEnvironment environment) {

        if (entires.containsKey(environment.getName())) {
            return ;
        }

        entires.put(environment.getName(), environment.getType());

        environment.setNamingResources(this);

        envs.put(environment.getName(), environment);
    }

    public void addResourceParams(ResourceParams resourceParamters) {

        if (resourcesParams.containsKey(resourceParamters.getName())) {
            return;
        }
        resourceParamters.setResources(this);
        resourcesParams.put(resourceParamters.getName(), resourceParamters);
    }
}
