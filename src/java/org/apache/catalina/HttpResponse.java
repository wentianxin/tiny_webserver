package org.apache.catalina;

import javax.servlet.http.Cookie;

/**
 * 与之对应是 HttpServletResponse
 */
public interface HttpResponse extends Response{

    public Cookie[] getCookies();

    public String getHeader(String name);

    public String[] getHeaderNames();

    public String[] getHeaderValues(String name);

    public String getMessage();

    public int getStatus();

    public void reset(int status, String message);
}
