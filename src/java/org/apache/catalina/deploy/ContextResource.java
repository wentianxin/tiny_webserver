package org.apache.catalina.deploy;

/**
 * Created by tisong on 9/8/16.
 */
public class ContextResource {

    /**
     * 该资源所需要的权限(<code>Application</code> 或者 <code>Container</code>)
     */
    private String auth = null;

    /**
     * 资源的描述
     */
    private String description = null;

    /**
     * 资源的名字
     */
    private String name = null;

    /**
     * Shareable 或者 UnShareable
     */
    private String scope = "Shareable";

    /**
     * 资源的类型
     */
    private String type = null;


    private NamingResources resources = null;




    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NamingResources getResources() {
        return resources;
    }

    public void setResources(NamingResources resources) {
        this.resources = resources;
    }



}
