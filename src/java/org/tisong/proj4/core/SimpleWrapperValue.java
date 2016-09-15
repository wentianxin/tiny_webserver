//package org.tisong.proj4.core;
//
//import org.apache.catalina.*;
//
//import javax.servlet.Servlet;
//import javax.servlet.ServletException;
//import java.io.IOException;
//
///**
// * Created by tisong on 8/9/16.
// */
//public class SimpleWrapperValue implements Value, Contained{
//
//    private Container container;
//
//    @Override
//    public void invoke(Request request, Response response, ValueContext valueContext) throws IOException, ServletException {
//
//        SimpleWrapper wrapper = (SimpleWrapper) getContainer();
//
//        Servlet servlet = null;
//
//        servlet = wrapper.allocate();
//
//        servlet.service(request, response);
//    }
//
//    @Override
//    public Container getContainer() {
//        return container;
//    }
//
//    @Override
//    public void setContainer(Container container) {
//        this.container = container;
//    }
//}
