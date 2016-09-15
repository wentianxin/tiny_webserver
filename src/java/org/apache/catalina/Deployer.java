package org.apache.catalina;

import java.io.IOException;
import java.net.URL;

/**
 * Created by tisong on 9/9/16.
 */
public interface Deployer {


    public String getName();


    public void install(String contextPath, URL war);



    public void install(URL config, URL war) throws IOException;



    public Context findDepolyedApp(String contextPath);


    public String[] findDepolyedApps();




    public void remove(String contextPath) throws IOException;


    public void start(String contextPath) throws IOException;



    public void stop(String contextPath) throws IOException;
}
