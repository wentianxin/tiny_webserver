package org.tisong.proj4.core;

import org.apache.catalina.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Context容器流水线执行过程中的阀门: SimpleContextValue
 * 调用过程: 在SimpleContext的 invoke最后的调用方法
 *
 * Created by tisong on 8/9/16.
 */
public class SimpleContextValue implements Value, Contained{

    private Container container;


    // --------------------------------------------------------- Implements Value

    /**
     * 目的: 执行 context子容器 wrapper的invoke方法
     * 实现思路: 找到 request 对应的 Wrapper对象 (找到对应的 Mapper对象: SimpleContextMapper; 调用 map方法,
     *           实际调用的是SimpleContext的 findChild 方法)
     * @param request
     * @param response
     * @param valueContext
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void invoke(Request request, Response response, ValueContext valueContext) throws IOException, ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) request.getRequest();

        Context context = (Context) getContainer();

        Wrapper wrapper = null;

        wrapper = (Wrapper) context.map(request, true);

        if (wrapper == null) {

            return ;
        }

        wrapper.invoke(request, response);
    }


    // --------------------------------------------------------- Implements Contained
    @Override
    public Container getContainer() {
        return this.container;
    }

    @Override
    public void setContainer(Container container) {
        this.container = container;
    }
}
