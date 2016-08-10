package org.apache.catalina;

/**
 * Created by tisong on 8/9/16.
 */
public interface Context extends Container{

    public void addLoader(Loader loader);

    public Loader getLoader();

    public void addServletMapping(String pattern, String name);

}
