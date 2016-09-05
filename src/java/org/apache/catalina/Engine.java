package org.apache.catalina;

/**
 * Created by tisong on 9/4/16.
 */
public interface Engine extends Container{

    public String getDefaultHost();

    public void setDefaultHost(String defaultHost);


}
