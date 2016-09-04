package org.apache.catalina;

import javax.servlet.http.Cookie;
import java.util.Locale;

/**
 * 与之相对应的是 HttpServletRequest
 */
public interface HttpRequest extends Request{

    // --------------------------------------------------------- Public Methods


    public void addCookie(Cookie cookie);

    public void addHeader(String name, String value);

    public void addParameter(String name, String values[]);



    public void clearCookies();

    public void clearHeaders();

    public void clearLocales();

    public void clearParameters();



    public void setAuthType(String type);

    public void setContextPath(String path);

    public void setMethod(String method);

    public void setQueryString(String query);

    public void setPathInfo(String path);

    public void setRequestedSessionCookie(boolean flag);

    public void setRequestedSessionId(String id);

    public void setRequestedSessionURL(boolean flag);

    public void setRequestURI(String uri);

    public void setServletPath(String path);
}
