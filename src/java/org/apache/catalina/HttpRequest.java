package org.apache.catalina;

import javax.servlet.http.Cookie;
import java.util.Locale;

/**
 * Created by tisong on 9/3/16.
 */
public interface HttpRequest extends Request{

    public void addCookie(Cookie cookie);


    public void addHeader(String name, String value);



    public void addLoacal(Locale locale);



    public void addParameter(String name, String[] values);


    public void clearCookies();


    public void clearHeaders();


    public void clearLocales();


    public void clearParameters();



    public void setAuthType(String type);


    public void setMethod(String method);

    public void setQueryString(String query);


    public void setRequestURI(String uri);





}
