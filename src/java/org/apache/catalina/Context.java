package org.apache.catalina;

import javax.servlet.ServletContext;

/**
 * path: 指定Web应用的URL入口
 * docBase: 指定Web应用的文件路径, 可以给定绝对路径; 也可以是 Host下appBase的相对路径
 * Created by tisong on 8/9/16.
 */
public interface Context extends Container{


    // ------------------------------------------------------------- Properties

    public boolean isAvailable();

    public void setAvailable(boolean available);

    public String getDocBase();
    public void setDocBase(String docBase);

    public String getPath();
    public void setPath(String path);

    public boolean getReloadable();
    public void setReloadable(boolean reloadable);

    public int getSessionTimeout();
    public void setSessionTimeout(int timeout);


    public ServletContext getServletContext();


    public void addLoader(Loader loader);

    public Loader getLoader();



    public void addServletMapping(String pattern, String name);

    public String findServletMapping(String pattern);

    public String[] findServletMappings();
}
