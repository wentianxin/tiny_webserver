package org.apache.catalina.deploy;

import java.util.HashMap;
import java.util.Map;

/**
 * Filter信息的封装类
 * Created by tisong on 9/4/16.
 */
public final class FilterDef {


    private String description = null;

    private String displayName = null;


    private String filterClass = null;


    private String filterName = null;


    private Map<String, String> parameters = new HashMap<String, String>();



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFilterClass() {
        return filterClass;
    }

    public void setFilterClass(String filterClass) {
        this.filterClass = filterClass;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }


    public Map<String, String> getParameters() {
        return parameters;
    }

    public  void addInitParameter(String name, String value) {
        parameters.put(name, value);
    }


}
