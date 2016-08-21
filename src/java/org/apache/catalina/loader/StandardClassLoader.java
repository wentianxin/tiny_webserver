package org.apache.catalina.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by tisong on 8/16/16.
 */
public class StandardClassLoader extends URLClassLoader {

    public StandardClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public StandardClassLoader(URL[] urls) {
        super(urls);
    }
}
