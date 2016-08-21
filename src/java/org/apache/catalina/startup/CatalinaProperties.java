package org.apache.catalina.startup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by tisong on 8/20/16.
 */
public class CatalinaProperties {


    private static Properties properties = null;


    private static void loadProperties() {

        InputStream is = null;

        if (is != null) {
            try {
                properties = new Properties();
                properties.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Enumeration<?> enumeration = properties.propertyNames();
        while(enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            String value = properties.getProperty(name);
            if (value != null) {
                System.setProperty(name, value);
            }
        }
    }
}
