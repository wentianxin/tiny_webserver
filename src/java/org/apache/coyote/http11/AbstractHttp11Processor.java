package org.apache.coyote.http11;

import org.apache.coyote.AbstractProcessor;
import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
import org.apache.tomcat.util.net.SocketWrapper;

import java.io.IOException;

/**
 * Created by tisong on 8/22/16.
 */
public abstract class AbstractHttp11Processor<S> extends AbstractProcessor<S> {


    @Override
    public SocketState process(SocketWrapper<S> socketWrapper) throws IOException{
        // 将 socket 解析成对应的 request, response

        // 进行字符串的验证 过滤


        adapter.service(request, response);
    }
}
