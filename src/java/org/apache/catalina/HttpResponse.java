package org.apache.catalina;

import javax.servlet.http.Cookie;

/**
 * Created by tisong on 9/3/16.
 */
public interface HttpResponse extends Response{


    public Cookie[] getCookies();


    public String getHeader(String name);


    public String[] getHeaderValues(String name);


    public int getStatus();



}
