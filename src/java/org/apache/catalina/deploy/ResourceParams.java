package org.apache.catalina.deploy;

import java.util.Hashtable;

/**
 * Created by tisong on 9/15/16.
 */
public class ResourceParams {

    private String name = null;

    private Hashtable resourceParams = new Hashtable();


    private NamingResources resources = null;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hashtable getResourceParams() {
        return resourceParams;
    }

    public void setResourceParams(Hashtable resourceParams) {
        this.resourceParams = resourceParams;
    }

    public NamingResources getResources() {
        return resources;
    }

    public void setResources(NamingResources resources) {
        this.resources = resources;
    }
}
