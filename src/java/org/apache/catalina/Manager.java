package org.apache.catalina;

import java.io.IOException;

/**
 * Created by tisong on 9/5/16.
 */
public interface Manager {

    public Container getContainer();

    public void setContainer(Container container);

    public int getMaxInactiveInterval();

    public void setMaxInactiveInterval(int interval);

    // --------------------------------------------------------- Public Methods

    public void add(Session session);

    public void remove(Session session);

    public Session createSession();


    public Session findSession(String id) throws IOException;


    public Session[] findSessions();


    public void load() throws IOException;

    public void unload() throws IOException;
}
