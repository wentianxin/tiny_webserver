package org.apache.catalina.core;

import javax.servlet.SessionCookieConfig;

/**
 * Created by tisong on 8/22/16.
 */
public class ApplicationSessionCookieConfig implements SessionCookieConfig{

    private StandardContext context;

    public ApplicationSessionCookieConfig(StandardContext context) {
        this.context = context;
    }


    @Override
    public void setName(String name) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setDomain(String domain) {

    }

    @Override
    public String getDomain() {
        return null;
    }

    @Override
    public void setPath(String path) {

    }

    @Override
    public String getPath() {
        return null;
    }

    @Override
    public void setComment(String comment) {

    }

    @Override
    public String getComment() {
        return null;
    }

    @Override
    public void setHttpOnly(boolean httpOnly) {

    }

    @Override
    public boolean isHttpOnly() {
        return false;
    }

    @Override
    public void setSecure(boolean secure) {

    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public void setMaxAge(int MaxAge) {

    }

    @Override
    public int getMaxAge() {
        return 0;
    }
}
