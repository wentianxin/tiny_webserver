package org.apache.catalina.core;

import org.apache.catalina.Container;
import org.apache.catalina.Host;
import org.apache.catalina.Mapper;
import org.apache.catalina.Request;
import org.apache.catalina.util.StringManager;

/**
 * Created by tisong on 9/5/16.
 */
public class StandardEngineMapper implements Mapper{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);

    private String protocol = null;

    private StandardEngine engine = null;


    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Override
    public Container getContainer() {
        return this.engine;
    }

    @Override
    public void setContainer(Container container) {
        this.engine = (StandardEngine)container;
    }

    @Override
    /**
     * 根据ServerName 匹配Host
     */
    public Container map(Request request, boolean update) {

        String serverName = request.getRequest().getServerName();
        if (serverName == null) {
            serverName = engine.getDefaultHost();
            if (update) {
                request.setServerName(serverName);
            }
        }

        if (serverName == null) {
            return null;
        }

        Host host = (Host) engine.findChild(serverName);
        if (host == null) {
            // 试着从 alias中匹配 Host

        }

        if (host == null) {
            host = (Host) engine.findChild(engine.getDefaultHost());
        }

        return host;
    }
}
