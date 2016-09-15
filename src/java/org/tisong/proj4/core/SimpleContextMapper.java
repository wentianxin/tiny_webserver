//package org.tisong.proj4.core;
//
//import org.apache.catalina.*;
//
///**
// * 根据 request 请求对象匹配到 （调用 context.findChild(containerName)）对象的 Wrapper 容器
// * 作用： 解析 request 请求的 wrapper 容器的路径名
// *
// * Created by tisong on 8/9/16.
// */
//public class SimpleContextMapper implements Mapper{
//
//    private Context context;
//
//
//    @Override
//    public String getProtocol() {
//        return null;
//    }
//
//    @Override
//    public void setProtocol(String protocol) {
//
//    }
//
//    @Override
//    public Container getContainer() {
//        return this.context;
//    }
//
//    @Override
//    public void setContainer(Container container) {
//        if (! (container instanceof SimpleContext) ) {
//            throw new IllegalArgumentException ("Illegal type of container");
//        }
//
//        this.context = (Context) container;
//    }
//
//
//    @Override
//    public Container map(Request request, boolean update) {
//
//        Wrapper wrapper = null;
//
//        String contextPath = request.getRequest().getContextPath();
//
//        String requestURI = request.getDecodedRequestURI();
//
//        String relativeURI = requestURI.substring(contextPath.length());
//
//
//
//
//        String servletPath = relativeURI;
//        String name = context.findServletMapping(relativeURI);
//        if (name != null) {
//            wrapper = (Wrapper) context.findChild(name);
//        }
//
//        return wrapper;
//    }
//}
