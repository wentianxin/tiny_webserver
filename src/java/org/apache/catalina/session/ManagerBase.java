package org.apache.catalina.session;

import org.apache.catalina.Container;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;

import java.io.IOException;

/**
 * Created by tisong on 9/15/16.
 */
public class ManagerBase implements Manager {




    @Override
    public Container getContainer() {
        return null;
    }

    @Override
    public void setContainer(Container container) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {

    }

    @Override
    public void add(Session session) {

    }

    @Override
    public void remove(Session session) {

    }

    @Override
    public Session createSession() {
        return null;
    }

    @Override
    public Session findSession(String id) throws IOException {
        return null;
    }

    @Override
    public Session[] findSessions() {
        return new Session[0];
    }

    @Override
    public void load() throws IOException {

    }

    @Override
    public void unload() throws IOException {

    }
}
