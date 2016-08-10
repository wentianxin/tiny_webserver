package org.tisong.proj3.connector.http;

/**
 * 一个 HTTP 请求报文由 request line, header, black, request data组成
 * Created by tisong on 8/7/16.
 */
public final class HttpHeader {

    private static final int INITIAL_NAME_SIZE = 32;
    private static final int INITIAL_VALUE_SIZE = 64;
    private static final int MAX_NAME_SIZE = 128;
    private static final int MAX_VALUE_SIZE = 4096;

    public char[] name;
    public char[] value;
    public int    nameEnd;
    public int    valueEnd;

    public HttpHeader() {
        this(new char[INITIAL_NAME_SIZE], 0, new char[INITIAL_VALUE_SIZE], 0);
    }

    public HttpHeader(char[] name, int nameEnd, char[] value, int valueEnd) {
        this.name = name;
        this.nameEnd = nameEnd;
        this.value = value;
        this.valueEnd = valueEnd;
    }


    public void recyle() {
        nameEnd  = 0;
        valueEnd = 0;
    }
}
