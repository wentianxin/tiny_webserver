package org.apache.catalina.connector;

import org.apache.catalina.HttpRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 *
 */
public class HttpRequestFacade
    extends RequestFacade
    implements HttpServletRequest{

    public HttpRequestFacade(HttpRequest request) {
        super(request);
    }



    @Override
    public String getAuthType() {
        return ((HttpServletRequest) request).getAuthType();
    }

    @Override
    public Cookie[] getCookies() {
        return ((HttpServletRequest) request).getCookies();
    }

    @Override
    public long getDateHeader(String name) {
        return ((HttpServletRequest) request).getDateHeader(name);
    }

    @Override
    public String getHeader(String name) {
        return ((HttpServletRequest) request).getHeader(name);
    }

    @Override
    public Enumeration getHeaders(String name) {
        return ((HttpServletRequest) request).getHeaders(name);
    }

    @Override
    public Enumeration getHeaderNames() {
        return ((HttpServletRequest) request).getHeaderNames();
    }

    @Override
    public int getIntHeader(String name) {
        return ((HttpServletRequest) request).getIntHeader(name);
    }

    @Override
    public String getMethod() {
        return ((HttpServletRequest) request).getMethod();
    }

    @Override
    public String getPathInfo() {
        return ((HttpServletRequest) request).getPathInfo();
    }

    @Override
    public String getPathTranslated() {
        return ((HttpServletRequest) request).getPathTranslated();
    }

    @Override
    public String getContextPath() {
        return ((HttpServletRequest) request).getContextPath();
    }

    @Override
    public String getQueryString() {
        return ((HttpServletRequest) request).getQueryString();
    }

    @Override
    public String getRemoteUser() {
        return ((HttpServletRequest) request).getRemoteUser();
    }

    @Override
    public boolean isUserInRole(String role) {
        return ((HttpServletRequest) request).isUserInRole(role);
    }

    @Override
    public Principal getUserPrincipal() {
        return ((HttpServletRequest) request).getUserPrincipal();
    }

    @Override
    public String getRequestedSessionId() {
        return ((HttpServletRequest) request).getRequestedSessionId();
    }

    @Override
    public String getRequestURI() {
        return ((HttpServletRequest) request).getRequestURI();
    }

    @Override
    public StringBuffer getRequestURL() {
        return ((HttpServletRequest) request).getRequestURL();
    }

    @Override
    public String getServletPath() {
        return ((HttpServletRequest) request).getServletPath();
    }

    @Override
    public HttpSession getSession(boolean create) {
        return ((HttpServletRequest) request).getSession(create);
    }

    @Override
    public HttpSession getSession() {
        return ((HttpServletRequest) request).getSession();
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return ((HttpServletRequest) request).isRequestedSessionIdValid();
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return ((HttpServletRequest) request).isRequestedSessionIdFromCookie();
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return ((HttpServletRequest) request).isRequestedSessionIdFromURL();
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return ((HttpServletRequest) request).isRequestedSessionIdFromUrl();
    }

    @Override
    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }

    @Override
    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }

    @Override
    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        request.setCharacterEncoding(env);
    }

    @Override
    public int getContentLength() {
        return request.getContentLength();
    }

    @Override
    public String getContentType() {
        return request.getContentType();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return request.getInputStream();
    }

    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    @Override
    public Enumeration getParameterNames() {
        return request.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
    }

    @Override
    public Map getParameterMap() {
        return request.getParameterMap();
    }

    @Override
    public String getProtocol() {
        return request.getProtocol();
    }

    @Override
    public String getScheme() {
        return request.getScheme();
    }

    @Override
    public String getServerName() {
        return request.getServerName();
    }

    @Override
    public int getServerPort() {
        return request.getServerPort();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return request.getReader();
    }

    @Override
    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }

    @Override
    public String getRemoteHost() {
        return request.getRemoteHost();
    }

    @Override
    public void setAttribute(String name, Object o) {
        request.setAttribute(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        request.removeAttribute(name);
    }

    @Override
    public Locale getLocale() {
        return request.getLocale();
    }

    @Override
    public Enumeration getLocales() {
        return request.getLocales();
    }

    @Override
    public boolean isSecure() {
        return request.isSecure();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return request.getRequestDispatcher(path);
    }

    @Override
    public String getRealPath(String path) {
        return request.getRealPath(path);
    }

    @Override
    public int getRemotePort() {
        return request.getRemotePort();
    }

    @Override
    public String getLocalName() {
        return request.getLocalName();
    }

    @Override
    public String getLocalAddr() {
        return request.getLocalAddr();
    }

    @Override
    public int getLocalPort() {
        return request.getLocalPort();
    }
}
