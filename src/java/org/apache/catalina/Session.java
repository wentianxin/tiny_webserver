package org.apache.catalina;

import javax.servlet.http.HttpSession;

/**
 * Created by tisong on 9/5/16.
 */
public interface Session {

    // ------------------------------------------------------------- Properties
    /**
     * 创建时间
     */
    public long getCreationTime();
    public void setCreationTime(long time);

    /**
     * Session Id
     */
    public String getId();
    public void setId(String id);

    /**
     * 最后访问时期
     */
    public long getLastAccessedTime();

    /**
     * 关联的Manager
     */
    public Manager getManager();
    public void setManager();

    /**
     * 最大有效时间
     */
    public int getMaxInactiveInternal();
    public void setManInactiveInternal(int internal);


    /**
     * 关联的HttpSession对象
     */
    public HttpSession getSession();

    /**
     * 有效标志位
     */
    public void setVaild(boolean isVaild);
    public boolean isVaild();


    // --------------------------------------------------------- Public Methods

    public void access();


    public void addSessionListener(SessionListener listener);

    public void removeSessionListener(SessionListener listener);

    public void recycle();



}
