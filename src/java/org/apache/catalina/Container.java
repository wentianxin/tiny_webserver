package org.apache.catalina;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * <ul>
 * 容器相关组件
 * <li>加载器<code>Loader</code></li>
 * <li>Session管理器<code>Manage</code></li>
 * <li><code>Mapper</code></li>
 * <li>子容器</li>
 * <li>流水线<code>Pipeline</code></li>
 * </ul>
 * Created by tisong on 8/9/16.
 */
public interface Container {

    // -------------------------------------------------------- Properties

    public void setName(String name);

    public String getName();

    public void setParent(Container container);

    public Container getParent();


    public Manager getManager();

    public void setManager(Manager manager);

    public Loader getLoader();

    public void setLoader(Loader loader);


    public void setParentClassLoader(ClassLoader parent);

    public ClassLoader getParentClassLoader();


    // ------------------------------------------------------- Public Methods

    public void addChild(Container child);

    public void removeChild(Container child);


    public Container findChild(String name);

    public Container[] findChildren();

    public void addMapper(Mapper mapper);

    public void removeMapper(Mapper mapper);

    public Mapper findMapper(String protocol);

    public Mapper[] findMappers();

    public Container map(Request request, boolean update);


    public void invoke(Request request, Response response) throws IOException, ServletException;



}
