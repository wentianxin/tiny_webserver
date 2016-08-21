package org.apache.coyote.http11;

/**
 * Created by tisong on 8/21/16.
 */
public abstract class AbstractHttp11JsseProtocol extends AbstractHttp11Protocol{

    @Override
    public void init() throws Exception {

        // TODO
        // SSL implementation needs to be in place before end point is
        // initialized
        // sslImplementation = SSLImplementation.getInstance(sslImplementationName);

        super.init();
    }
}
