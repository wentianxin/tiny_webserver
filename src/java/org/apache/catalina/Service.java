package org.apache.catalina;

/**
 * 服务: 提供多个连接器使得 tomcat 支持多种协议(HTTP HTTPS等)
 */
public interface Service {

    /**
     * 关联的容器: Connector会将处理的 Request 传递给该容器
     */
    public Container getContainer();
    public void setContainer(Container container);

    /**
     * Service Name
     */
    public String getName();
    public void setName(String name);

    /**
     * 关联的连接器 (一对多, 每个连接器实现一种协议)
     */
    public void addConnector(Connector connector);
    public Connector[] findConnectors();
    public void removeConnector(Connector connector);

    /**
     * 启动前调用,初始化 Service; 为 Connectors 连接器绑定监听的端口
     * @throws LifecycleException
     */
    public void initialize() throws LifecycleException;
}
