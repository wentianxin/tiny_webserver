package org.apache.catalina.connector;

import org.apache.catalina.Response;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tisong on 9/4/16.
 */
public final class HttpResponseFacade
    extends ResponseFacade
    implements HttpServletResponse{


    public HttpResponseFacade(Response response) {
        super(response);

    }


    @Override
    public void addCookie(Cookie cookie) {

        ((HttpServletResponse) response).addCookie(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
        return ((HttpServletResponse)response).containsHeader(name);
    }

    @Override
    public String encodeURL(String url) {
        return ((HttpServletResponse)response).encodeURL(url);
    }

    @Override
    public String encodeRedirectURL(String url) {
        return ((HttpServletResponse)response).encodeRedirectURL(url);
    }

    @Override
    public String encodeUrl(String url) {
        return ((HttpServletResponse)response).encodeURL(url);
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return ((HttpServletResponse)response).encodeRedirectURL(url);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        ((HttpServletResponse)response).sendError(sc, msg);
    }

    @Override
    public void sendError(int sc) throws IOException {
        ((HttpServletResponse)response).sendError(sc);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        ((HttpServletResponse)response).sendRedirect(location);
    }

    @Override
    public void setDateHeader(String name, long date) {
        ((HttpServletResponse)response).setDateHeader(name, date);
    }

    @Override
    public void addDateHeader(String name, long date) {
        ((HttpServletResponse)response).addDateHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
        ((HttpServletResponse) response).setHeader(name, value);
    }


    @Override
    public void addHeader(String name, String value) {
        ((HttpServletResponse)response).addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
        ((HttpServletResponse)response).setIntHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
        ((HttpServletResponse)response).addIntHeader(name, value);
    }

    @Override
    public void setStatus(int sc) {
        ((HttpServletResponse)response).setStatus(sc);
    }

    @Override
    public void setStatus(int sc, String sm) {
        ((HttpServletResponse)response).setStatus(sc, sm);
    }
}
