package org.apache.tomcat;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;

/**
 * Created by tisong on 8/20/16.
 */
public interface JarScannerCallback {

    public void scan(JarURLConnection urlConn) throws IOException;

    public void scan(File file) throws IOException;

}
