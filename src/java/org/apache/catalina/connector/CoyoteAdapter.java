package org.apache.catalina.connector;

import org.apache.coyote.Adapter;
import org.apache.coyote.Request;
import org.apache.coyote.Response;

/**
 * Created by tisong on 8/21/16.
 */
public class CoyoteAdapter implements Adapter {

    private Connector connector = null;

    public CoyoteAdapter(Connector connector) {

        super();
        this.connector = connector;
    }


    @Override
    public void service(Request req, Response res) throws Exception {

    }
}
