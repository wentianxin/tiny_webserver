package org.apache.catalina.connector;

import org.apache.coyote.Adapter;

import java.io.IOException;

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
    public void service(org.apache.coyote.Request  req, org.apache.coyote.Response res) throws Exception {


        Request request = (Request) req.getNote(ADAPTER_NOTES);

        Response response = (Response) res.getNote(ADAPTER_NOTES);

        if (request == null) {

            // catalina connector 创建 request 对象
            request = connector.createRequest();
            request.setCoyoteRequest(req);
            response = connector.createResponse();
            response.setCoyoteResponse(res);

            // 互相链接
            reqest.setResponse(response);
            reponse.setRequest(request);

            // Set as notes
            req.setNote(ADAPTER_NOTES, request);
            res.setNote(ADAPTER_NOTES, response);

            // Set query string encoding
            req.getParameters().setQueryStringEncoding
                    (connector.getURIEncoding());

        }


        boolean comet = false;
        boolean async = false;

        try {
            // req → request res → response

            boolean postParseSuccess = postParseRequest(req, request, res, response);
            if (postParseSuccess) {

                //check valves if we support async

                // Calling the container
                connector.getService().getContainer().getPipeline().getFirst().invoke(request, response);

                if (request.isComet()) {

                }
            }


            AsyncContextImpl asyncConImpl = (AsyncContextImpl) request.getAsyncContext();
            if (asyncConImpl != null) {
                async = true;
            } else if (!comet) {
                request.finishRequest();
                response.finshResponse();


                req.action(ActionCode.POST_REQUEST, null);
            }

        } catch (IOException e) {

        } finally {

            // recycle the wrapper request and response
            if (!comet && !async) {
                request.recycle();
                response.recycle();
            } else {

            }
        }
    }



}
