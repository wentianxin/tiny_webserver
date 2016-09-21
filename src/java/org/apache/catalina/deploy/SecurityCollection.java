package org.apache.catalina.deploy;

import org.apache.catalina.util.RequestUtil;

/**
 * Created by tisong on 9/20/16.
 */
public class SecurityCollection {


    private String name = null;


    private String[] patterns = new String[0];

    private String[] methods = new String[0];



    private String descriptions = null;




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public String[] getMethods() {
        return methods;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }



    public void addMethod(String method) {
        if (method == null)
            return;
        String results[] = new String[methods.length + 1];
        for (int i = 0; i < methods.length; i++)
            results[i] = methods[i];
        results[methods.length] = method;
        methods = results;
    }

    public void addPattern(String pattern) {
        if (pattern == null)
            return;
        pattern = RequestUtil.URLDecode(pattern);
        String results[] = new String[patterns.length + 1];
        for (int i = 0; i < patterns.length; i++)
            results[i] = patterns[i];
        results[patterns.length] = pattern;
        patterns = results;
    }
}
