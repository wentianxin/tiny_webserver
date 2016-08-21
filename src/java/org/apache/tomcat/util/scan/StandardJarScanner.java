package org.apache.tomcat.util.scan;

import org.apache.catalina.startup.Constants;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by tisong on 8/20/16.
 */
public class StandardJarScanner implements JarScanner {


    @Override
    public void scan(ServletContext context, ClassLoader classLoader, JarScannerCallback callback, Set<String> jarsToSkip) {


        // Scan WEB-INF/lib
        Set<String> dirList = context.getResourcePaths(Constants.WEB_INF_LIB);
        if (dirList != null) {
            Iterator<String> it = dirList.iterator();
            while (it.hasNext()) {
                String path = it.next();

                if (path.endsWith(Constants.JAR_EXT)) {

                    URL url = null;

                    process(callback, url);
                } else {

                }
            }
        }
    }

    private void process(JarScannerCallback callback, URL url)
        throws IOException {

    }
}
