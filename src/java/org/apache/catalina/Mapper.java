package org.apache.catalina;

/**
 * Mapper 属性
 * 1. 协议
 * 2. 绑定的容器
 * Created by tisong on 8/9/16.
 */
public interface Mapper {


    // ------------------------------------------------------------- Properties

    public String getProtocol();

    public void setProtocol(String protocol);


    public Container getContainer();

    public void setContainer(Container container);


    // --------------------------------------------------------- Public Methods

    public Container map(Request request, boolean update);
}
