package org.apache.catalina.util;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by tisong on 9/15/16.
 */
public class ServerInfo {

    private static String serverInfo = null;

    static {
        InputStream is = null;

        try {
            is = ServerInfo.class.getResourceAsStream("/org/apache/catalina/util/ServerInfo.properties");

            Properties properties = new Properties();

            properties.load(is);

        } catch (Exception e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (serverInfo == null)
            serverInfo = "Apache Tomcat";
    }

    public static String getServerInfo() {
        return serverInfo;
    }
}
