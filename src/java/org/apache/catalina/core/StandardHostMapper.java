package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.catalina.util.StringManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by tisong on 9/5/16.
 */
public class StandardHostMapper implements Mapper{

    private static final StringManager sm =
            StringManager.getManager(Constants.Package);

    private StandardHost host = null;

    private String protocol = null;


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
        return this.host;
    }

    @Override
    public void setContainer(Container container) {
        this.host = (StandardHost) container;
    }

    /**
     * 根据请求 URI 获取 Context 容器
     * @param request
     * @param update
     * @return
     */
    @Override
    public Container map(Request request, boolean update) {

        String requestURI = ((HttpServletRequest)(request.getRequest())).getRequestURI();

        Context context = host.map(requestURI);

        if (update) {
            request.setContext(context); // TODO request 关联 Context 的作用是什么?
            if (context != null) {
                ((HttpRequest) request).setContextPath(context.getPath());
            } else {
                ((HttpRequest) request).setContextPath(null);
            }
        }
        return  context;
    }
}
