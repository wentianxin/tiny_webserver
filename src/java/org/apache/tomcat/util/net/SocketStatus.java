package org.apache.tomcat.util.net;

/**
 * Socket 过程的某一阶段的状态
 */
public enum SocketStatus {
    OPEN, STOP, TIMEOUT, DISCONNECT, ERROR
}
