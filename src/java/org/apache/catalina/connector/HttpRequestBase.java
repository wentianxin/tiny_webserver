package org.apache.catalina.connector;

import org.apache.catalina.Context;
import org.apache.catalina.HttpRequest;
import org.apache.catalina.Session;
import org.apache.catalina.Wrapper;
import org.apache.catalina.util.Enumerator;
import org.apache.catalina.util.ParameterMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;

/**
 * Created by tisong on 9/4/16.
 */
public class HttpRequestBase
    extends RequestBase
    implements HttpRequest, HttpServletRequest{



    protected String contextPath = "";


    protected String method = null;

    protected String requestURI = null;
    protected String queryString = null;


    protected ParameterMap parameters = null;

    protected Map<String, List> headers = new HashMap<String, List>();

    protected List<Cookie> cookies = new ArrayList<Cookie>();


    protected String servletPath = null;


    protected String pathInfo = null;

    protected Session session = null;

    /**
     * Session Id
     */
    protected String requestedSessionId = null;
    protected boolean requestedSessionURL = false;
    protected boolean requestedSessionCookie = false;



    protected HttpRequestFacade facade = new HttpRequestFacade(this);


    protected boolean parsed = false;


    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        // TODO 比较好奇 如果直接返回 cookies对象会如何
//        synchronized (cookies) {
//            if (cookies.size() < 1)
//                return (null);
//            Cookie results[] = new Cookie[cookies.size()];
//            return ((Cookie[]) cookies.toArray(results));
//        }

        if (cookies.size() < 1) {
            return null;
        }
        Cookie results[] = new Cookie[cookies.size()];

        return cookies.toArray(results);
    }

    @Override
    public long getDateHeader(String name) {
        return 0;
    }


    @Override
    public String getHeader(String name) {

        List values = headers.get(name);

        if (values != null) {
            return (String) values.get(0);
        }

        return null;
    }
    @Override
    public Enumeration getHeaders(String name) {

        List values = headers.get(name);
        if (values != null) {
            return new Enumerator(values);
        } else {
            return new Enumerator(new ArrayList());
        }
    }

    @Override
    public Enumeration getHeaderNames() {
        return (new Enumerator(headers.keySet()));
    }



    @Override
    public int getIntHeader(String name) {
        return 0;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getPathInfo() {
        return this.pathInfo;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return this.contextPath;
    }

    @Override
    public String getQueryString() {
        return this.queryString;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return this.requestedSessionId;
    }

    @Override
    public String getRequestURI() {
        return this.requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer url = new StringBuffer();
        String scheme = getScheme();
        int port = getServerPort();
        if (port < 0)
            port = 80; // Work around java.net.URL bug

        url.append(scheme);
        url.append("://");
        url.append(getServerName());
        if ((scheme.equals("http") && (port != 80))
                || (scheme.equals("https") && (port != 443))) {
            url.append(':');
            url.append(port);
        }
        url.append(getRequestURI());

        return (url);
    }

    @Override
    public String getServletPath() {
        return this.servletPath;
    }

    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    /**
     * 返回<code>true</code> 如果 Session id 被包含在 Cookie中
     */
    @Override
    public boolean isRequestedSessionIdFromCookie() {
        if (requestedSessionId != null)
            return (requestedSessionCookie);
        else
            return (false);
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }





    // ---------------------------------------- Implements
    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public void addHeader(String name, String value) {
        ArrayList values = (ArrayList) headers.get(name);
        if (values == null) {
            values = new ArrayList();
            headers.put(name, values);
        }
        values.add(value);
    }

    @Override
    public void addParameter(String name, String[] values) {
        parameters.put(name, values);
    }

    @Override
    public void clearCookies() {

    }

    @Override
    public void clearHeaders() {

    }

    @Override
    public void clearLocales() {

    }

    @Override
    public void clearParameters() {

    }

    @Override
    public void setAuthType(String type) {

    }

    @Override
    public void setContextPath(String path) {
        if (path == null)
            this.contextPath = "";
        else
            this.contextPath = path;
    }

    @Override
    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public void setQueryString(String query) {
        this.queryString = query;
    }

    @Override
    public void setPathInfo(String path) {
        this.pathInfo = path;
    }

    @Override
    public void setRequestedSessionCookie(boolean flag) {
        this.requestedSessionCookie = flag;
    }

    @Override
    public void setRequestedSessionId(String id) {
        this.requestedSessionId = id;
    }

    @Override
    public void setRequestedSessionURL(boolean flag) {
        this.requestedSessionURL = flag;
    }

    @Override
    public void setRequestURI(String uri) {
        this.requestURI = uri;
    }

    @Override
    public void setServletPath(String path) {
        this.servletPath = path;
    }

    @Override
    public String getParameter(String name) {
        parseParameters();
        String values[] = (String[]) parameters.get(name);
        if (values != null)
            return (values[0]);
        else
            return (null);
    }

    @Override
    public Enumeration getParameterNames() {
        parseParameters();
        return (new Enumerator(parameters.keySet()));
    }

    @Override
    public String[] getParameterValues(String name) {
        parseParameters();
        String values[] = (String[]) parameters.get(name);
        if (values != null)
            return (values);
        else
            return (null);
    }

    @Override
    public Map getParameterMap() {
        parseParameters();
        return (this.parameters);
    }


    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public void setContext(Context context) {

    }

    @Override
    public Context getContext() {
        return null;
    }


    @Override
    public Wrapper getWrapper() {
        return this.wrapper;
    }

    @Override
    public void setWrapper(Wrapper wrapper) {
        this.wrapper = wrapper;
    }

    protected void parseParameters() {
        if (parsed) {
            return ;
        }
    }


    @Override
    public ServletRequest getRequest() {
        return facade;
    }
}
