package org.apache.catalina;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

/**
 * Created by tisong on 8/9/16.
 */
public interface Wrapper extends Container{

    /**
     * 返回 Servlet 可用的 date/time
     * 如果 date/time 是未来时期, 则返回 SC_SERVICE_UNAVAILABLE
     * 如果 date/time 是 0, 则 servlet 当前是可用的
     * 如果 date/time 是Long.MAX_VALUE, 则永久不可使用
     * @return
     */
    public long getAvailable();

    public void setAvailable(long available);

    public boolean isUnavailable();


    public void setServletClass(String servletClass);

    public String getServletClass();



    public Servlet allocate() throws ServletException;
    public void deallocate(Servlet servlet) throws ServletException;


    public void load() throws ServletException;
    public void unload() throws ServletException;
}
