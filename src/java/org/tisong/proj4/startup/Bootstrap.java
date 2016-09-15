//package org.tisong.proj4.startup;
//
//import org.apache.catalina.*;
//import org.apache.catalina.connector.http.HttpConnector;
//import org.tisong.proj4.core.*;
//
///**
// * Context-Wrapper
// *      Connetcor - 连接器
// *      Mapper    - 协议
// *      Loader    - 类加载器
// *      LifecycleListener - 生命周期监听器
// */
//public class Bootstrap {
//
//    public static void main(String[] args) {
//        Connector connector = new HttpConnector();
//
//        Wrapper wrapper1 = new SimpleWrapper();
//        wrapper1.setName("Primitive");
//        wrapper1.setServletClass("PrimitiveServlet");
//        Wrapper wrapper2 = new SimpleWrapper();
//        wrapper2.setName("Modern");
//        wrapper2.setServletClass("ModernServlet");
//
//        Loader loader = new SimpleLoader();
//
//        LifecycleListener listener = new SimpleContextLifecycleListener();
//
//        Mapper mapper = new SimpleContextMapper();
//        mapper.setProtocol("http");
//
//        Context context = new SimpleContext();
//        context.addChild(wrapper1);
//        context.addChild(wrapper2);
//        ((Lifecycle)context).addLifecycleListener(listener);
//        context.addMapper(mapper);
//        context.addLoader(loader);
//        context.addServletMapping("/Primitive", "Primitive");
//        context.addServletMapping("/Modern", "Modern");
//
//
//        connector.setContainer(context);
//
//        try {
//            /* 初始化连接器 组件 */
//            connector.initialize();
//
//            ((Lifecycle) connector).start();
//
//            ((Lifecycle) context).start();
//        } catch (Exception e) {
//
//        }
//
//
//    }
//}
