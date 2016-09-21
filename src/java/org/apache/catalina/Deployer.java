package org.apache.catalina;

import java.io.IOException;
import java.net.URL;

/**
 * Created by tisong on 9/9/16.
 */
public interface Deployer {


    public static final String PRE_INSTALL_EVENT = "pre-install";

    public static final String INSTALL_EVENT = "install";

    public static final String REMOVE_EVENT = "remove";



    public String getName();


    public void install(String contextPath, URL war);



    public void install(URL config, URL war) throws IOException;



    public Context findDepolyedApp(String contextPath);


    public String[] findDepolyedApps();




    public void remove(String contextPath) throws IOException;


    public void start(String contextPath) throws IOException;



    public void stop(String contextPath) throws IOException;
}
