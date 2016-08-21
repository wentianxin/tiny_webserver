package org.apache.catalina.startup;

import org.apache.catalina.loader.StandardClassLoader;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tisong on 8/16/16.
 */
public class ClassLoaderFactory {


    public static ClassLoader createClassLoader(List<Repository> repositories, final ClassLoader parent)
        throws Exception {


        Set<URL> set = new LinkedHashSet<URL>();



        final URL[] array = set.toArray(new URL[set.size()]);


        return AccessController.doPrivileged(
                new PrivilegedAction<ClassLoader>() {
                    @Override
                    public ClassLoader run() {
                        if (parent == null) {
                            return new StandardClassLoader(array);
                        } else {
                            return new StandardClassLoader(array, parent);
                        }
                    }
                }
        );
    }

    public static enum RepositoryType {
        DIR,
        GLOB,
        JAR,
        URL
    }

    public static class Repository {
        private String location;
        private RepositoryType type;

        public Repository(String location, RepositoryType type) {
            this.location = location;
            this.type = type;
        }

        public String getLocation() {
            return location;
        }

        public RepositoryType getType() {
            return type;
        }
    }
}
