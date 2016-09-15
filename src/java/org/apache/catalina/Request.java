package org.apache.catalina;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by tisong on 8/10/16.
 */
public interface Request {

    // ------------------------------------------------------------- Properties


    public String getAuthorization();

    public void setAuthorization(String authorization);


    public Connector getConnector();

    public void setConnector(Connector connector);



    public ServletRequest getRequest();


    public Response getResponse();

    public void setResponse(Response response);



    public Socket getSocket();


    public InputStream getStream();
    public void setStream(InputStream stream);




    // --------------------------------------------------------- Public Methods


    public ServletInputStream createInputStream() throws IOException;


    public void finishRequest() throws IOException;


    public void recycle();



    public void setContentLength(int length);


    public void setContentType(String type);


    public void setProtocol(String protocol);


    public void setScheme(String scheme);

    public void setServerName(String name);

    public void setServerPort(int port);


    public void setContext(Context context);
    public Context getContext();

}
