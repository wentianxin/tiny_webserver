//package org.apache.tomcat.util.net;
//
//import java.io.IOException;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.ServerSocketChannel;
//import java.nio.channels.SocketChannel;
//import java.nio.channels.spi.SelectorProvider;
//import java.util.Iterator;
//
///**
// * Created by tisong on 9/1/16.
// */
//public class NioEndpoint extends AbstractEndpoint{
//
//
//    private ServerSocketChannel serverSock = null;
//
//    private Poller[] pollers = null;
//
//    @Override
//    public void bind() throws Exception {
//        serverSock = SelectorProvider.provider().openServerSocketChannel();
//
//        serverSock.socket().bind(addr, getBacklog());
//
//        serverSock.configureBlocking(true);
//
//
//    }
//
//    @Override
//    public void unbind() throws Exception {
//
//    }
//
//    @Override
//    public void startInternal() throws Exception {
//
//    }
//
//    @Override
//    public void stopInternal() throws Exception {
//
//
//        pollers = new Poller[getPollerThreadCount()];
//
//        for (int i = 0; i < pollers.length; i++) {
//
//        }
//
//        startAcceptorThreads();
//    }
//
//
//    protected boolean setSocketOptions(SocketChannel socket) {
//        // Step 1 ： 设置socket 属性
//
//        // Step 2 ： 将 socket 包装成 NioChannel（SocketChannel → NioChannel 的过度 why??? ）
//        // TODO why?
//
//
//        // Step 3 ： 生产者 与 消费者模型 - 向 Poller 队列中添加事件
//    }
//
//    public boolean processSocket(NioChannel socket, SocketStatus status, boolean dispatch) {
//
//    }
//
//    public class Poller implements Runnable {
//
//        private Selector selector;
//
//        public Poller() throws IOException {
//            // Selector.open() isn't thread safe
//            this.selector = Selector.open();
//        }
//        @Override
//        public void run() {
//            // 要多次检测是否有事件添加到队列中
//            try {
//                keyCount = selector.select(selectorTimeout);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            Iterator<SelectionKey> iterator =
//                    keyCount > 0 ? selector.selectedKeys().iterator() : null;
//
//            while (iterator != null && iterator.hasNext()) {
//                SelectionKey sk = iterator.next();
//
//                KeyAttachment attachment = (KeyAttachment) sk.attachment();
//
//                iterator.remove();
//
//                processKey(sk, attachment);
//            }
//        }
//
//        protected boolean processKey(SelectionKey sk, KeyAttachment attachment) {
//
//        }
//
//    }
//
//
//    protected class Acceptor extends AbstractEndpoint.Acceptor {
//
//        @Override
//        public void run() {
//
//            SocketChannel socket = null;
//
//            try {
//                socket = serverSock.accept();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//
//            setSocketOptions(socket);
//        }
//    }
//
//    public static class KeyAttachment extends SocketWrapper<NioChannel> {
//
//    }
//}
