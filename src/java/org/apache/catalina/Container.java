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


    public static final String ADD_CHILD_EVENT = "addChild";

    public static final String REMOVE_CHILD_EVENT = "removeChild";




    // -------------------------------------------------------- Properties


    /**
     * 容器名
     */
    public void setName(String name);
    public String getName();

    /**
     * 关联父类
     * @param container
     */
    public void setParent(Container container);
    public Container getParent();

    /**
     * 父类加载器
     */
    public void setParentClassLoader(ClassLoader parent);

    /**
     * 获取父类的类加载器; 如果为null, 则调用父类的 <code>getParentClassLoader</code>方法; 如果父类为null, 则返回默认的系统类加载器
     */
    public ClassLoader getParentClassLoader();


    /**
     * Realm 组件
     */
    public Realm getRealm();
    public void setRealm(Realm realm);

    /**
     * Logger 组件
     */
    public Logger getLogger();
    public void setLogger(Logger logger);


    /**
     * Manager 组件
     */
    public Manager getManager();
    public void setManager(Manager manager);

    /**
     * Loader 组件
     */
    public Loader getLoader();
    public void setLoader(Loader loader);




    // ------------------------------------------------------- Public Methods

    /**
     * 对子容器的增、删、查
     */
    public void addChild(Container child);
    public void removeChild(Container child);
    public Container findChild(String name);
    public Container[] findChildren();


    /**
     * 对Mapper的管理
     * @param mapper
     */
    public void addMapper(Mapper mapper);
    public void removeMapper(Mapper mapper);
    public Mapper findMapper(String protocol);
    public Mapper[] findMappers();


    public Container map(Request request, boolean update);


    public void invoke(Request request, Response response) throws IOException, ServletException;



}
