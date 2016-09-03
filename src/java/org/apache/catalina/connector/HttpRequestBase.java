package org.apache.catalina.connector;

import org.apache.catalina.HttpRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;

/**
 * Created by tisong on 9/3/16.
 */
public class HttpRequestBase extends RequestBase
        implements HttpRequest, HttpServletRequest {



    // ------------------------------- implements HttpRequest

    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public void addHeader(String name, String value) {

    }

    @Override
    public void addLoacal(Locale locale) {

    }

    @Override
    public void addParameter(String name, String[] values) {

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
    public void setMethod(String method) {

    }

    @Override
    public void setQueryString(String query) {

    }

    @Override
    public void setRequestURI(String uri) {

    }


    // ------------------------------------------ implements HttpServletRequest


    @Override
    public String getAuthType() {
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader(String s) {
        return 0;
    }

    @Override
    public String getHeader(String s) {
        return null;
    }

    @Override
    public Enumeration getHeaders(String s) {
        return null;
    }

    @Override
    public Enumeration getHeaderNames() {
        return null;
    }

    @Override
    public int getIntHeader(String s) {
        return 0;
    }

    @Override
    public String getMethod() {
        return null;
    }

    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }

    @Override
    public String getQueryString() {
        return null;
    }

    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String s) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        return null;
    }

    @Override
    public String getRequestURI() {
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        return null;
    }

    @Override
    public String getServletPath() {
        return null;
    }

    @Override
    public HttpSession getSession(boolean b) {
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

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }



}
