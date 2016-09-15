package org.apache.catalina.core;

import org.apache.catalina.Context;
import org.apache.catalina.deploy.FilterDef;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Enumeration;

/**
 * Created by tisong on 9/4/16.
 */
public class ApplicationFilterConfig implements FilterConfig {

    private Context context = null;

    private Filter filter = null;

    private FilterDef filterDef = null;

    /**
     * 关联组件; 关联 FilterDef; 实例化Filter对象
     * @param context
     * @param filterDef
     */
    public ApplicationFilterConfig(Context context, FilterDef filterDef) throws ClassNotFoundException, InstantiationException, ServletException, IllegalAccessException {

        this.context = context;
        setFilterDef(filterDef);
    }
    @Override
    public String getFilterName() {
        return filterDef.getFilterName();
    }

    @Override
    public ServletContext getServletContext() {
        return this.context.getServletContext();
    }

    @Override
    public String getInitParameter(String name) {
        return null;
    }

    @Override
    public Enumeration getInitParameterNames() {
        return null;
    }

    /**
     * 根据FilterDef信息来实例化Filter对象
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws ServletException
     */
    public Filter getFilter() throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, ServletException {

        if (this.filter != null) {
            return this.filter;
        }

        String filterClass = filterDef.getFilterClass();
        ClassLoader classLoader = null;
        // TODO 为什么要这样区分
        if (filterClass.startsWith("org.apache.catalina.")) {
            classLoader = this.getClass().getClassLoader();
        } else {
            classLoader = context.getLoader().getClassLoader();
        }

        Class clazz = classLoader.loadClass(filterClass);

        this.filter = (Filter) clazz.newInstance();

        filter.init(this);

        return filter;
    }


    public void setFilterDef(FilterDef filterDef) throws ClassNotFoundException, InstantiationException, ServletException, IllegalAccessException {
        this.filterDef = filterDef;

        if (filterDef == null) {
            if (this.filter != null) {
                this.filter.destroy();
            }
            this.filter = null;
        } else {
            getFilter();
        }
    }
}
