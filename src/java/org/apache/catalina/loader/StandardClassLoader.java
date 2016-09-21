package org.apache.catalina.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by tisong on 9/19/16.
 */
public class StandardClassLoader
    extends URLClassLoader
    implements Reloader{

    public StandardClassLoader() {

        super(new URL[0]);
//        this.parent = getParent();
//        this.system = getSystemClassLoader();
//        securityManager = System.getSecurityManager();

    }


    @Override
    public boolean modified() {
        return false;
    }
}
