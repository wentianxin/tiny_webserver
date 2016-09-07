package org.apache.catalina.connector.http10;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.HttpRequest;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.util.LifecycleSupport;
import org.apache.catalina.util.StringManager;

public final class HttpProcessor
        implements Lifecycle, Runnable {


    // ----------------------------------------------------- Manifest Constants

    private static final String SERVER_INFO =
            ServerInfo.getServerInfo() + " (HTTP/1.0 Connector)";


    // ----------------------------------------------------- Instance Variables

    protected StringManager sm =
            StringManager.getManager(Constants.Package);

    private int id = 0;

    private HttpConnector connector = null;

    private LifecycleSupport lifecycle = new LifecycleSupport(this);

    private HttpRequestImpl request = null;

    private HttpResponseImpl response = null;

    private int serverPort = 0;


    private Socket socket = null;

    private boolean available = false;

    private boolean started = false;

    private boolean stopped = false;


    private Thread thread = null;

    private String threadName = null;

    private Object threadSync = new Object();



    // ----------------------------------------------------------- Constructors

    public HttpProcessor(HttpConnector connector, int id) {

        super();
        this.connector = connector;
        this.id = id;
        this.request = (HttpRequestImpl) connector.createRequest();
        this.response = (HttpResponseImpl) connector.createResponse();
        this.serverPort = connector.getPort();
        this.threadName =
                "HttpProcessor[" + connector.getPort() + "][" + id + "]";
    }


    // ---------------------------------------------- Background Thread Methods

    public void run() {

        // Process requests until we receive a shutdown signal
        while (!stopped) {

            // Wait for the next socket to be assigned
            Socket socket = await();
            if (socket == null)
                continue;

            // Process the request from this socket
            process(socket);

            // Finish up this request
            request.recycle();
            response.recycle();
            connector.recycle(this);

        }

        // Tell threadStop() we have shut ourselves down successfully
        synchronized (threadSync) {
            threadSync.notifyAll();
        }

    }

    // ------------------------------------------------------ Lifecycle Methods


    public void addLifecycleListener(LifecycleListener listener) {

        lifecycle.addLifecycleListener(listener);

    }


    public LifecycleListener[] findLifecycleListeners() {

        return lifecycle.findLifecycleListeners();

    }


    public void removeLifecycleListener(LifecycleListener listener) {

        lifecycle.removeLifecycleListener(listener);

    }

    public void start() throws LifecycleException {

        if (started)
            throw new LifecycleException
                    (sm.getString("httpProcessor.alreadyStarted"));
        lifecycle.fireLifecycleEvent(START_EVENT, null);
        started = true;

        threadStart();

    }

    public void stop() throws LifecycleException {

        if (!started)
            throw new LifecycleException
                    (sm.getString("httpProcessor.notStarted"));
        lifecycle.fireLifecycleEvent(STOP_EVENT, null);
        started = false;

        threadStop();

    }


    // -------------------------------------------------------- Package Methods
    public synchronized void assign(Socket socket) {

        // Wait for the Processor to get the previous Socket
        while (available) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        // Store the newly available Socket and notify our thread
        this.socket = socket;
        available = true;
        notifyAll();
    }

    // -------------------------------------------------------- Private Methods
    private synchronized Socket await() {

        // Wait for the Connector to provide a new Socket
        while (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        Socket socket = this.socket;
        available = false;
        notifyAll();

        return socket;

    }

    private void process(Socket socket) {

        boolean ok = true;
        InputStream input = null;
        OutputStream output = null;

        // Construct and initialize the objects we will need
        try {
            input = new BufferedInputStream(socket.getInputStream(),
                    connector.getBufferSize());
            request.setStream(input);
            request.setResponse(response);
            output = socket.getOutputStream();
            response.setStream(output);
            response.setRequest(request);
            ((HttpServletResponse) response.getResponse()).setHeader
                    ("Server", SERVER_INFO);
        } catch (Exception e) {
            log("process.create", e);
            ok = false;
        }

        // Parse the incoming request
        try {
            if (ok) {
                parseConnection(socket);
                parseRequest(input);
                if (!request.getRequest().getProtocol().startsWith("HTTP/0"))
                    parseHeaders(input);
            }
        } catch (Exception e) {
            try {
                ((HttpServletResponse) response.getResponse()).sendError
                        (HttpServletResponse.SC_BAD_REQUEST);
            } catch (Exception f) {

            }
        }


        try {
            if (ok) {
                connector.getContainer().invoke(request, response);
            }
        } catch (ServletException e) {
            try {
                ((HttpServletResponse) response.getResponse()).sendError
                        (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception f) {
                ;
            }
            ok = false;
        } catch (Throwable e) {
            try {
                ((HttpServletResponse) response.getResponse()).sendError
                        (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception f) {

            }
            ok = false;
        }

        try {
            if (ok)
                response.finishResponse();
        } catch (IOException e) {

        }

        try {
            if (output != null)
                output.flush();
        } catch (IOException e) {

        }

        try {
            if (output != null)
                output.close();
        } catch (IOException e) {

        }


        try {
            if (ok)
                request.finishRequest();
        } catch (IOException e) {

        }
        try {
            if (input != null)
                input.close();
        } catch (IOException e) {

        }

        try {
            socket.close();
        } catch (IOException e) {

        }
    }

    private String readLine(InputStream input) throws IOException {

        StringBuffer sb = new StringBuffer();
        while (true) {
            int ch = input.read();
            if (ch < 0) {
                if (sb.length() == 0) {
                    return (null);
                } else {
                    break;
                }
            } else if (ch == '\r') {
                continue;
            } else if (ch == '\n') {
                break;
            }
            sb.append((char) ch);
        }

        return (sb.toString());

    }

    private void parseHeaders(InputStream input)
            throws IOException, ServletException {

        while (true) {

            // Read the next header line
            String line = read(input);
            if ((line == null) || (line.length() < 1))
                break;

            // Parse the header name and value
            int colon = line.indexOf(':');
            if (colon < 0)
                throw new ServletException
                        (sm.getString("httpProcessor.parseHeaders.colon"));
            String name = line.substring(0, colon).trim();
            String match = name.toLowerCase();
            String value = line.substring(colon + 1).trim();


            // Set the corresponding request headers
            if (match.equals("authorization")) {

            } else if (match.equals("accept-language")) {



            } else if (match.equals("cookie")) {

            } else if (match.equals("content-length")) {

            } else if (match.equals("content-type")) {

            } else if (match.equals("host")) {

            } else {
                request.addHeader(name, value);
            }
        }

    }

    private void parseRequest(InputStream input)
            throws IOException, ServletException {


        String line = readLine(input);
        if (line == null) {
            throw new ServletException
                    (sm.getString("httpProcessor.parseRequest.read"));
        }
        StringTokenizer st = new StringTokenizer(line);

        String method = null;
        try {
            method = st.nextToken();
        } catch (NoSuchElementException e) {
            method = null;
        }

        String uri = null;
        try {
            uri = st.nextToken();
        } catch (NoSuchElementException e) {
            uri = null;
        }

        String protocol = null;
        try {
            protocol = st.nextToken();
        } catch (NoSuchElementException e) {
            protocol = "HTTP/0.9";
        }

        // Validate the incoming request line
        if (method == null) {
            throw new ServletException
                    (sm.getString("httpProcessor.parseRequest.method"));
        } else if (uri == null) {
            throw new ServletException
                    (sm.getString("httpProcessor.parseRequest.uri"));
        }

        // Parse any query parameters out of the request URI
        int question = uri.indexOf('?');
        if (question >= 0) {
            request.setQueryString(uri.substring(question + 1));
            if (debug >= 1)
                log(" Query string is " +
                        ((HttpServletRequest) request.getRequest()).getQueryString());
            uri = uri.substring(0, question);
        } else
            request.setQueryString(null);

        // Parse any requested session ID out of the request URI
        int semicolon = uri.indexOf(match);
        if (semicolon >= 0) {
            String rest = uri.substring(semicolon + match.length());
            int semicolon2 = rest.indexOf(';');
            if (semicolon2 >= 0) {
                request.setRequestedSessionId(rest.substring(0, semicolon2));
                rest = rest.substring(semicolon2);
            } else {
                request.setRequestedSessionId(rest);
                rest = "";
            }
            request.setRequestedSessionURL(true);
            uri = uri.substring(0, semicolon) + rest;
            if (debug >= 1)
                log(" Requested URL session id is " +
                        ((HttpServletRequest) request.getRequest()).getRequestedSessionId());
        } else {
            request.setRequestedSessionId(null);
            request.setRequestedSessionURL(false);
        }

        // Set the corresponding request properties
        ((HttpRequest) request).setMethod(method);
        request.setProtocol(protocol);
        ((HttpRequest) request).setRequestURI(uri);

        request.setScheme("http");      // No SSL support

    }


    // ------------------------

    private void threadStart() {

        thread = new Thread(this, threadName);
        thread.setDaemon(true);
        thread.start();

    }

    private void threadStop() {


        stopped = true;
        assign(null);
        synchronized (threadSync) {
            try {
                threadSync.wait(5000);
            } catch (InterruptedException e) {
                ;
            }
        }
        thread = null;

    }

}
