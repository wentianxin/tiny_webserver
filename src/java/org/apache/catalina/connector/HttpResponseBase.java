package org.apache.catalina.connector;

import org.apache.catalina.HttpResponse;

import javax.servlet.http.Cookie;
import java.io.IOException;

/**
 * Created by tisong on 9/3/16.
 */
public class HttpResponseBase
    extends ResponseBase
    implements HttpResponse{


    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public String[] getHeaderValues(String name) {
        return new String[0];
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public void finshResponse() throws IOException {

    }

    @Override
    public int getContextLength() {
        return 0;
    }
}
