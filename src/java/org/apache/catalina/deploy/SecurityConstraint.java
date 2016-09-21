package org.apache.catalina.deploy;

/**
 * Created by tisong on 9/20/16.
 */
public class SecurityConstraint {

    private SecurityCollection[] collections = new SecurityCollection[0];


    private String[] authRoles = new String[0];

    private boolean allRoles = false;

    private boolean authConstraint = false;


    private String displayName = null;

    /**
     * 简单的数组扩容
     */
    public void addCollection(SecurityCollection collection) {
        if (collection == null)
            return;
        SecurityCollection results[] =
                new SecurityCollection[collections.length + 1];
        for (int i = 0; i < collections.length; i++)
            results[i] = collections[i];
        results[collections.length] = collection;
        collections = results;
    }


    public void addAuthRole(String authRole) {
        if (authRole == null)
            return;
        if ("*".equals(authRole)) {
            allRoles = true;
            return;
        }
        String results[] = new String[authRoles.length + 1];
        for (int i = 0; i < authRoles.length; i++)
            results[i] = authRoles[i];
        results[authRoles.length] = authRole;
        authRoles = results;
        authConstraint = true;
    }


    public void setAuthConstraint(boolean authConstraint) {
        this.authConstraint = authConstraint;
    }
}
