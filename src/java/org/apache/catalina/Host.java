package org.apache.catalina;

/**
 * 一个域名解析到那个虚拟主机进行配置
 * name: 项目的域名（alias标签用来配置其他域名）
 * appBase: 虚拟目录的路径(用来运行jsp的目录); 指定项目父路径;
 *
 * Created by tisong on 9/4/16.
 */
public interface Host extends Container{

    public String getAppBase();
    public void setAppBase(String appBase);



    public boolean getAutoDeploy();
    public void setAutoDeploy(boolean autoDeploy);


    public Context map(String uri);



    public String[] findAliases();

    public void removeAlias(String alias);


    public void importDefaultContext(Context context);
}
