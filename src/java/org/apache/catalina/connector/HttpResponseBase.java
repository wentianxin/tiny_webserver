package org.apache.catalina.connector;

import org.apache.catalina.Connector;
import org.apache.catalina.Context;
import org.apache.catalina.HttpResponse;
import org.apache.catalina.Response;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tisong on 9/4/16.
 */
public class HttpResponseBase
    extends ResponseBase
    implements HttpResponse, HttpServletResponse{


    protected Map<String, List> headers = new HashMap<String, List>();

    protected List cookies = new ArrayList();

    protected int status = HttpServletResponse.SC_OK;




    protected HttpResponseFacade facade = new HttpResponseFacade(this);



    protected Connector connector = null;

    protected Context context = null;


    @Override
    public void addCookie(Cookie cookie) {

    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return null;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {

    }

    @Override
    public void sendError(int sc) throws IOException {

    }

    @Override
    public void sendRedirect(String location) throws IOException {

    }

    @Override
    public void setDateHeader(String name, long date) {

    }

    @Override
    public void addDateHeader(String name, long date) {

    }

    @Override
    public void setHeader(String name, String value) {

        if (included) {
            return ;
        }

        ArrayList values = new ArrayList();
        values.add(value);
        synchronized (headers) {
            headers.put(name, values);
        }

        String match = name.toLowerCase();
        if (match.equals("content-length")) {
            int contentLength = -1;
            try {
                contentLength = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                ;
            }
            if (contentLength >= 0)
                setContentLength(contentLength);
        } else if (match.equals("content-type")) {
            setContentType(value);
        }
    }

    @Override
    public void addHeader(String name, String value) {
        if (included) {
            return ;
        }

        List values = headers.get(name);
        if (values == null) {
            values = new ArrayList();
            headers.put(name, values);
        }
        values.add(value);
    }

    @Override
    public void setIntHeader(String name, int value) {

    }

    @Override
    public void addIntHeader(String name, int value) {

    }

    @Override
    public void setStatus(int sc) {

    }

    @Override
    public void setStatus(int sc, String sm) {

    }



    // ------------------------------------------ Implements HttpResponse

    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public String[] getHeaderNames() {
        return new String[0];
    }

    @Override
    public String[] getHeaderValues(String name) {
        return new String[0];
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public void reset(int status, String message) {

    }

    public ServletResponse getResponse() {
        return facade;
    }
}
