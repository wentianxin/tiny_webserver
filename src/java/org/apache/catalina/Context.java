package org.apache.catalina;

import org.apache.catalina.deploy.*;
import org.apache.catalina.util.CharsetMapper;

import javax.servlet.ServletContext;

/**
 * path: 指定Web应用的URL入口
 * docBase: 指定Web应用的文件路径, 可以给定绝对路径; 也可以是 Host下appBase的相对路径
 * Created by tisong on 8/9/16.
 */
public interface Context extends Container{


    // ------------------------------------------------------------- Properties




    public Object[] getApplicationListeners();
    public void setApplicationListeners(Object[] listeners);
    public String[] findApplicationListeners();
    public void addApplicationListener(String listener);
    public void removeApplicationListener(String listener);


    public void addConstraint(SecurityConstraint constraint);
    public SecurityConstraint[] findConstraints();
    public void removeConstraint(SecurityConstraint constraint);


    public void getLoginConfig();
    public void setLoginConfig(LoginConfig config);


    public CharsetMapper getCharsetMapper();
    public void setCharsetMapper(CharsetMapper mapper);


    public boolean getCookies();
    public void setCookies(boolean cookies);


    public String getDisplayName();
    public void setDisplayName(String displayName);


    public String getPublicId();
    public void setPublicId(String publicId);

    /**
     * Parameter 标签
     */
    public void addApplicationParameter(ApplicationParameter parameter);
    public ApplicationParameter[] findApplicationParameters();
    public void removeApplicationParameter(String name);


    /**
     * Context 状态
     */
    public boolean isAvailable();
    public void setAvailable(boolean available);


    /**
     * docBase 属性
     */
    public String getDocBase();
    public void setDocBase(String docBase);


    /**
     * path 属性
     */
    public String getPath();
    public void setPath(String path);


    /**
     * reloadable 属性
     */
    public boolean getReloadable();
    public void setReloadable(boolean reloadable);


    /**
     * Session Time 属性
     */
    public int getSessionTimeout();
    public void setSessionTimeout(int timeout);


    /**
     * 关联的 Server 上下文环境(每个Context 应用含有一个该实例对象)
     */
    public ServletContext getServletContext();


    /**
     * 关联的 Loader 组件
     */
    public void addLoader(Loader loader);
    public Loader getLoader();


    /**
     * Servlet 管理
     */
    public void addServletMapping(String pattern, String name);
    public String findServletMapping(String pattern);
    public String[] findServletMappings();
    public void removeServletMapping(String pattern);

    /**
     * 关联的 JNDI 资源对象总类
     */
    public NamingResources getNamingResources();
    public void setNamingResources(NamingResources namingResources);






    public void setConfigured(boolean configured);
    public boolean getConfigured();


    public void setOverride(boolean override);
    public boolean getOverride();


    public Wrapper createWrapper();


    public String getWrapperClass();
    public void setWrapperClass(String wrapperClass);




    // -------------------------------- JNDI 相关资源

    public void addEjb(ContextEjb ejb);
    public ContextEjb findEjb(String name);
    public ContextEjb[] findEjbs();
    public void removeEjb(String name);


    public void addEnvironment(ContextEnvironment environment);
    public ContextEnvironment findEnvironment(String name);
    public ContextEnvironment[] findEnvironments();
    public void removeEnvironment(String name);


    public void addLocalEjb(ContextLocalEjb ejb);
    public ContextLocalEjb findLocalEjb(String name);
    public ContextLocalEjb[] findLocalEjbs();



    public void addResource(ContextResource resource);
    public ContextResource findResource(String name);
    public ContextResource[] findResources();
    public void removeResource(String name);


    public void addResourceLink(ContextResourceLink resourceLink);
    public ContextResourceLink findResourceLink(String name);
    public ContextResourceLink[] findResourceLinks();
    public void removeResourceLink(String name);



//    public void addErrorPage(ErrorPage errorPage);
//    public ErrorPage findErrorPage(int errorCode);
//    public ErrorPage findErrorPage(String exceptionType);
//    public ErrorPage[] findErrorPages();
//    public void removeErrorPage(ErrorPage errorPage);



    public FilterDef findFilterDef(String filterName);
    public FilterDef[] findFilterDefs();
    public void removeFilterDef(FilterDef filterDef);


//    public void addFilterMap(FilterMap filterMap);
//    public FilterMap[] findFilterMaps();
//    public void removeFilterMap(FilterMap filterMap);


    public void reload();

}
