package org.apache.catalina.deploy;

/**
 * Created by tisong on 9/15/16.
 */
public class ApplicationParameter {

    private String description = null;

    private String name = null;


    private String value = null;


    private boolean override = true;


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

    public boolean isOverride() {
        return override;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
