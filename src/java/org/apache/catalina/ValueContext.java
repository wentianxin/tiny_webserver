package org.apache.catalina;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * Created by tisong on 8/10/16.
 */
public interface ValueContext {

    // -------------------------------------------------- Public Methods

    public void invokeNext(Request request, Response response) throws IOException, ServletException;
}
