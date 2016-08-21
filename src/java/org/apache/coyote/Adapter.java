package org.apache.coyote;



/**
 * Created by tisong on 8/21/16.
 */
public interface Adapter {

    public void service(Request req, Response res) throws Exception;
}
