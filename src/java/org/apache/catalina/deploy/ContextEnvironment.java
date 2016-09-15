package org.apache.catalina.deploy;

/**
 * <Environment> 配置一些键值对变量，类似于<env-entry>
 * Created by tisong on 9/9/16.
 */
public class ContextEnvironment {

    private String description = null;


    private String name = null;


    private String value = null;

    private String type = null;

    /**
     * 是否允许 environment env 被部署描述符(web.xml)重写
     */
    private boolean override = true;


    protected NamingResources namingResources = null;



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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }

    public NamingResources getNamingResources() {
        return namingResources;
    }

    public void setNamingResources(NamingResources namingResources) {
        this.namingResources = namingResources;
    }
}
