package org.apache.tomcat;

import javax.servlet.ServletContext;
import java.util.Set;

/**
 * Created by tisong on 8/20/16.
 */
public interface JarScanner {


    /**
     * Scan the provided ServletContext and classloader for JAR files. Each JAR
     * file found will be passed to the callback handler to be processed.
     *
     * @param context       The ServletContext - used to locate and access
     *                      WEB-INF/lib
     * @param classLoader   The classloader - used to access JARs not in
     *                      WEB-INF/lib
     * @param callback      The handler to process any JARs found
     * @param jarsToSkip    List of JARs to ignore
     */
     public void scan(ServletContext context, ClassLoader classLoader,
                     JarScannerCallback callback, Set<String> jarsToSkip);
}
