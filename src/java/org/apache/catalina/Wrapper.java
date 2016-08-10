package org.apache.catalina;

/**
 * Created by tisong on 8/9/16.
 */
public interface Wrapper extends Container{

    public void setServletClass(String servletClass);

    public String getServletClass();


}
