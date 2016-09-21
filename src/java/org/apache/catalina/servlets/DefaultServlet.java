package org.apache.catalina.servlets;

import org.apache.catalina.Globals;
import org.apache.catalina.util.FastHttpDateFormat;
import org.apache.catalina.util.ServerInfo;
import org.apache.catalina.util.StringManager;
import org.apache.naming.resources.Resource;
import org.apache.naming.resources.ResourceAttributes;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by tisong on 9/21/16.
 */
public class DefaultServlet extends HttpServlet {

    protected static final String mimeSeparation = "CATALINA_MIME_BOUNDARY";

    protected static final String RESOURCES_JNDI_NAME = "java:/comp/Resources";


    protected boolean listings = true;

    protected int input = 2048;

    protected int output = 2048;


    protected String[] welcomes = new String[0];

    protected static URLEncoder urlEncoder;

    protected static StringManager sm =
            StringManager.getManager(Constants.Package);



    public void init() throws ServletException {

    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        serveResource(request, response, true);
    }


    protected void serveResource(HttpServletRequest request, HttpServletResponse response, boolean content)
            throws IOException, ServletException{

        String path = getRelativePath(request);

        DirContext resources = getResources();

        ResourceInfo resourceInfo = new ResourceInfo(path, resources);

        if (!resourceInfo.exists) {

            return ;
        }

        if (!resourceInfo.collection) {
            // 如果不是目录却以 / \\ 结尾
            if (path.endsWith("/") || (path.endsWith("\\"))) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        request.getRequestURI());
                return;
            }
        }


        if (resourceInfo.collection) {
            // check the welcome files list
            if (!request.getRequestURI().endsWith("/")) {
                // URI 并不是以 / 结尾的话， 进行重定向
                String redirectPath = path;
                String contextPath = request.getContextPath();
                if ((contextPath != null) && (!contextPath.equals("/"))) {
                    redirectPath = contextPath + redirectPath;
                }
                if (!(redirectPath.endsWith("/")))
                    redirectPath = redirectPath + "/";
                redirectPath = appendParameters(request, redirectPath);
                response.sendRedirect(redirectPath);
                return;
            }

            ResourceInfo welcomeFileInfo = checkWelcomeFiles(path, resources);
            if (welcomeFileInfo != null) {

            }
        } else {
            // Checking If headers
            boolean included = (request.getAttribute(Globals.CONTEXT_PATH_ATTR) != null);
            //checkIfHeaders根据客户端请求头的If-None-Match，If-Modified-Since
            //来判断请求资源是否被修改，如果未被修改，则返回304头
            //客户端直接从客户端缓存中读取资源文件。
            if (!included
                    && !checkIfHeaders(request, response, resourceInfo)) {
                return;
            }
        }

        String contentType = getServletContext().getMimeType(resourceInfo.path);

        Vector ranges = null;


        if (resourceInfo.collection) {
            // Skip directory listings if we have been configured to
            // suppress them
            if (!listings) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                        request.getRequestURI());
                return;
            }
            contentType = "text/html;charset=UTF-8";

        } else {
            // Parse range specifier(If-Range Header)
            ranges = parseRange(request, response, resourceInfo);

            // ETag header
            response.setHeader("ETag", getETag(resourceInfo));

            // Last-Modified header
            response.setHeader("Last-Modified", resourceInfo.httpDate);
        }


        ServletOutputStream outputStream = null;

        PrintWriter writer = null;

        if (content) {
            try {
                outputStream = response.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if ( resourceInfo.collection || ( (ranges == null || ranges.isEmpty()) && request.getHeader("Range") == null)  ) {

            // Set the appropriate output headers
            if (contentType != null) {
                response.setContentType(contentType);
            }



            long contentLength = resourceInfo.length;
            if ((!resourceInfo.collection) && (contentLength >= 0)) {
                response.setContentLength((int) contentLength);
            }


            if (resourceInfo.collection) {
                if (content) {
                    // Serve the directory browser
                    resourceInfo.setStream(render(request.getContextPath(), resourceInfo));
                }
            }

            // Copy the input stream to our output stream (if requested)
            if (content) {
                try {
                    response.setBufferSize(output);
                } catch (IllegalStateException e) {
                    // Silent catch
                }
                if (outputStream != null) {
                    copy(resourceInfo, outputStream);
                } else {
                    copy(resourceInfo, writer);
                }
            }

        } else {
            if (ranges == null || ranges.isEmpty()) {
                return;
            }

            // Partial content response.
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

            if (ranges.size() == 1) {
                Range range = (Range) ranges.elementAt(0);
                response.addHeader("Content-Range", "bytes "
                        + range.start
                        + "-" + range.end + "/"
                        + range.length);
                response.setContentLength((int) (range.end - range.start + 1));

                if (contentType != null) {
                    response.setContentType(contentType);
                }

                if (content) {
                    try {
                        response.setBufferSize(output);
                    } catch (IllegalStateException e) {
                        // Silent catch
                    }
                    if (outputStream != null) {
                        copy(resourceInfo, outputStream, range);
                    } else {
                        copy(resourceInfo, writer, range);
                    }
                }
            } else {
                response.setContentType("multipart/byteranges; boundary="
                        + mimeSeparation);
                if (content) {
                    try {
                        response.setBufferSize(output);
                    } catch (IllegalStateException e) {
                        // Silent catch
                    }
                    if (outputStream != null) {
                        copy(resourceInfo, outputStream, ranges.elements(),
                                contentType);
                    } else {
                        copy(resourceInfo, writer, ranges.elements(),
                                contentType);
                    }
                }
            }

        }

    }

    protected boolean checkIfHeaders(HttpServletRequest request,
                                     HttpServletResponse response,
                                     ResourceInfo resourceInfo)
            throws IOException {

        return checkIfMatch(request, response, resourceInfo)
                && checkIfModifiedSince(request, response, resourceInfo)
                && checkIfNoneMatch(request, response, resourceInfo)
                && checkIfUnmodifiedSince(request, response, resourceInfo);

    }


    protected String getRelativePath(HttpServletRequest request) {

        // Are we being processed by a RequestDispatcher.include()?
        if (request.getAttribute("javax.servlet.include.request_uri")!=null) {
            String result = (String)
                    request.getAttribute("javax.servlet.include.path_info");
            if (result == null)
                result = (String)
                        request.getAttribute("javax.servlet.include.servlet_path");
            if ((result == null) || (result.equals("")))
                result = "/";
            return (result);
        }

        // No, extract the desired path directly from the request
        String result = request.getPathInfo();
        if (result == null) {
            result = request.getServletPath();
        }
        if ((result == null) || (result.equals(""))) {
            result = "/";
        }
        return normalize(result);

    }

    /**
     * Get resources. This method will try to retrieve the resources through
     * JNDI first, then in the servlet context if JNDI has failed (it could be
     * disabled). It will return null.
     *
     * @return A JNDI DirContext, or null.
     */
    protected DirContext getResources() {

        DirContext result = null;

        // Try the servlet context
        try {
            result = (DirContext) getServletContext()
                    .getAttribute(Globals.RESOURCES_ATTR);
        } catch (ClassCastException e) {
            // Failed : Not the right type
        }

        if (result != null)
            return result;

        // Try JNDI
        try {
            result =
                    (DirContext) new InitialContext().lookup(RESOURCES_JNDI_NAME);
        } catch (NamingException e) {
            // Failed
        } catch (ClassCastException e) {
            // Failed : Not the right type
        }

        return result;

    }


    /**
     * Return an InputStream to an HTML representation of the contents
     * of this directory.
     *
     * @param contextPath Context path to which our internal paths are
     *  relative
     */
    protected InputStream render
    (String contextPath, ResourceInfo resourceInfo) {

        String name = resourceInfo.path;

        // Number of characters to trim from the beginnings of filenames
        int trim = name.length();
        if (!name.endsWith("/"))
            trim += 1;
        if (name.equals("/"))
            trim = 1;

        // Prepare a writer to a buffered area
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OutputStreamWriter osWriter = null;
        try {
            osWriter = new OutputStreamWriter(stream, "UTF8");
        } catch (Exception e) {
            // Should never happen
            osWriter = new OutputStreamWriter(stream);
        }
        PrintWriter writer = new PrintWriter(osWriter);

        StringBuffer sb = new StringBuffer();

        // Render the page header
        sb.append("<html>\r\n");
        sb.append("<head>\r\n");
        sb.append("<title>");
        sb.append(sm.getString("directory.title", name));
        sb.append("</title>\r\n");
        sb.append("<STYLE><!--");
        sb.append("H1{font-family : sans-serif,Arial,Tahoma;color : white;background-color : #0086b2;} ");
        sb.append("H3{font-family : sans-serif,Arial,Tahoma;color : white;background-color : #0086b2;} ");
        sb.append("BODY{font-family : sans-serif,Arial,Tahoma;color : black;background-color : white;} ");
        sb.append("B{color : white;background-color : #0086b2;} ");
        sb.append("A{color : black;} ");
        sb.append("HR{color : #0086b2;} ");
        sb.append("--></STYLE> ");
        sb.append("</head>\r\n");
        sb.append("<body>");
        sb.append("<h1>");
        sb.append(sm.getString("directory.title", name));

        // Render the link to our parent (if required)
        String parentDirectory = name;
        if (parentDirectory.endsWith("/")) {
            parentDirectory =
                    parentDirectory.substring(0, parentDirectory.length() - 1);
        }
        int slash = parentDirectory.lastIndexOf('/');
        if (slash >= 0) {
            String parent = name.substring(0, slash);
            sb.append(" - <a href=\"");
            sb.append(rewriteUrl(contextPath));
            if (parent.equals(""))
                parent = "/";
            sb.append(rewriteUrl(parent));
            if (!parent.endsWith("/"))
                sb.append("/");
            sb.append("\">");
            sb.append("<b>");
            sb.append(sm.getString("directory.parent", parent));
            sb.append("</b>");
            sb.append("</a>");
        }

        sb.append("</h1>");
        sb.append("<HR size=\"1\" noshade>");

        sb.append("<table width=\"100%\" cellspacing=\"0\"" +
                " cellpadding=\"5\" align=\"center\">\r\n");

        // Render the column headings
        sb.append("<tr>\r\n");
        sb.append("<td align=\"left\"><font size=\"+1\"><strong>");
        sb.append(sm.getString("directory.filename"));
        sb.append("</strong></font></td>\r\n");
        sb.append("<td align=\"center\"><font size=\"+1\"><strong>");
        sb.append(sm.getString("directory.size"));
        sb.append("</strong></font></td>\r\n");
        sb.append("<td align=\"right\"><font size=\"+1\"><strong>");
        sb.append(sm.getString("directory.lastModified"));
        sb.append("</strong></font></td>\r\n");
        sb.append("</tr>");

        try {

            // Render the directory entries within this directory
            DirContext directory = resourceInfo.directory;
            NamingEnumeration namingEnumeration =
            resourceInfo.resources.list(resourceInfo.path);
            boolean shade = false;
            while (namingEnumeration.hasMoreElements()) {

                NameClassPair ncPair = (NameClassPair) namingEnumeration.nextElement();
                String resourceName = ncPair.getName();
                ResourceInfo childResourceInfo =
                        new ResourceInfo(resourceName, directory);

                String trimmed = resourceName/*.substring(trim)*/;
                if (trimmed.equalsIgnoreCase("WEB-INF") ||
                        trimmed.equalsIgnoreCase("META-INF"))
                    continue;

                sb.append("<tr");
                if (shade)
                    sb.append(" bgcolor=\"eeeeee\"");
                sb.append(">\r\n");
                shade = !shade;

                sb.append("<td align=\"left\">&nbsp;&nbsp;\r\n");
                sb.append("<a href=\"");
                sb.append(rewriteUrl(contextPath));
                resourceName = rewriteUrl(name + resourceName);
                sb.append(resourceName);
                if (childResourceInfo.collection)
                    sb.append("/");
                sb.append("\"><tt>");
                sb.append(trimmed);
                if (childResourceInfo.collection)
                    sb.append("/");
                sb.append("</tt></a></td>\r\n");

                sb.append("<td align=\"right\"><tt>");
                if (childResourceInfo.collection)
                    sb.append("&nbsp;");
                else
                    sb.append(renderSize(childResourceInfo.length));
                sb.append("</tt></td>\r\n");

                sb.append("<td align=\"right\"><tt>");
                sb.append(childResourceInfo.httpDate);
                sb.append("</tt></td>\r\n");

                sb.append("</tr>\r\n");
            }

        } catch (NamingException e) {
            // Something went wrong
            e.printStackTrace();
        }

        // Render the page footer
        sb.append("</table>\r\n");

        sb.append("<HR size=\"1\" noshade>");
        sb.append("<h3>").append(ServerInfo.getServerInfo()).append("</h3>");
        sb.append("</body>\r\n");
        sb.append("</html>\r\n");

        // Return an input stream to the underlying bytes
        writer.write(sb.toString());
        writer.flush();
        return (new ByteArrayInputStream(stream.toByteArray()));

    }


    /**
     * Render the specified file size (in bytes).
     *
     * @param size File size (in bytes)
     */
    protected String renderSize(long size) {

        long leftSide = size / 1024;
        long rightSide = (size % 1024) / 103;   // Makes 1 digit
        if ((leftSide == 0) && (rightSide == 0) && (size > 0))
            rightSide = 1;

        return ("" + leftSide + "." + rightSide + " kb");

    }


    protected String appendParameters(HttpServletRequest request,
                                      String redirectPath) {

        StringBuffer result = new StringBuffer(rewriteUrl(redirectPath));

        String query = request.getQueryString ();
        if (query != null)
            result.append ("?").append (query);

        return result.toString();

    }


    private ResourceInfo checkWelcomeFiles(String pathname, DirContext resources) {
        String collectionName = pathname;
        if (!pathname.endsWith("/")) {
            collectionName += "/";
        }

        synchronized (welcomes) {
            welcomes = (String[]) getServletContext().getAttribute
                    (Globals.WELCOME_FILES_ATTR);
            if (welcomes == null) {
                welcomes = new String[0];
            }
        }

        for (int i = 0; i < welcomes.length; i++) {

            // Does the specified resource exist?
            String resourceName = collectionName + welcomes[i];
            ResourceInfo resourceInfo =
                    new ResourceInfo(resourceName, resources);
            if (resourceInfo.exists()) {
                return resourceInfo;
            }
        }

        return null;
    }

    protected String getETag(ResourceInfo resourceInfo) {
        if (resourceInfo.strongETag != null) {
            return resourceInfo.strongETag;
        } else if (resourceInfo.weakETag != null) {
            return resourceInfo.weakETag;
        } else {
            return "W/\"" + resourceInfo.length + "-"
                    + resourceInfo.date + "\"";
        }
    }


    protected Vector parseRange(HttpServletRequest request,
                                HttpServletResponse response,
                                ResourceInfo resourceInfo)
            throws IOException {

        String headerValue = request.getHeader("If-Range");
        if (headerValue != null) {

        }

        long fileLength = resourceInfo.length;
        if (fileLength == 0) {
            return null;
        }

        String rangeHeader = request.getHeader("Range");
        if (rangeHeader == null) {
            return null;
        }

        rangeHeader = rangeHeader.substring(6);

        Vector result = new Vector();

        StringTokenizer commaTokenizer = new StringTokenizer(rangeHeader, ",");

        // Parsing the range list
        while (commaTokenizer.hasMoreTokens()) {
            String rangeDefinition = commaTokenizer.nextToken();

            Range currentRange = new Range();
            currentRange.length = fileLength;

            int dashPos = rangeDefinition.indexOf('-');

            if (dashPos == -1) {
                response.addHeader("Content-Range", "bytes */" + fileLength);
                response.sendError
                        (HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return null;
            }

            if (dashPos == 0) {
                try {
                    long offset = Long.parseLong(rangeDefinition);
                    currentRange.start = fileLength + offset;
                    currentRange.end = fileLength - 1;
                } catch (NumberFormatException e) {
                    response.addHeader("Content-Range",
                            "bytes */" + fileLength);
                    response.sendError
                            (HttpServletResponse
                                    .SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    return null;
                }

            } else {

                try {
                    currentRange.start = Long.parseLong
                            (rangeDefinition.substring(0, dashPos));
                    if (dashPos < rangeDefinition.length() - 1)
                        currentRange.end = Long.parseLong
                                (rangeDefinition.substring
                                        (dashPos + 1, rangeDefinition.length()));
                    else
                        currentRange.end = fileLength - 1;
                } catch (NumberFormatException e) {
                    response.addHeader("Content-Range",
                            "bytes */" + fileLength);
                    response.sendError
                            (HttpServletResponse
                                    .SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    return null;
                }

            }

            if (!currentRange.validate()) {
                response.addHeader("Content-Range", "bytes */" + fileLength);
                response.sendError
                        (HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return null;
            }

            result.addElement(currentRange);
        }

        return result;
    }


    private void copy(ResourceInfo resourceInfo, ServletOutputStream ostream)
            throws IOException {

        IOException exception = null;

        // FIXME : i18n ?
        InputStream resourceInputStream = resourceInfo.getStream();
        InputStream istream = new BufferedInputStream
                (resourceInputStream, input);

        // Copy the input stream to the output stream
        exception = copyRange(istream, ostream);

        // Clean up the input stream
        try {
            istream.close();
        } catch (Throwable t) {
            ;
        }

        // Rethrow any exception that has occurred
        if (exception != null)
            throw exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param resourceInfo The input stream to read from
     * @param writer The writer to write to
     *
     * @exception IOException if an input/output error occurs
     */
    private void copy(ResourceInfo resourceInfo, PrintWriter writer)
            throws IOException {

        IOException exception = null;

        InputStream resourceInputStream = resourceInfo.getStream();
        // FIXME : i18n ?
        Reader reader = new InputStreamReader(resourceInputStream);

        // Copy the input stream to the output stream
        exception = copyRange(reader, writer);

        // Clean up the reader
        try {
            reader.close();
        } catch (Throwable t) {
            ;
        }

        // Rethrow any exception that has occurred
        if (exception != null)
            throw exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param resourceInfo The ResourceInfo object
     * @param ostream The output stream to write to
     * @param range Range the client wanted to retrieve
     * @exception IOException if an input/output error occurs
     */
    private void copy(ResourceInfo resourceInfo, ServletOutputStream ostream,
                      Range range)
            throws IOException {

        IOException exception = null;

        InputStream resourceInputStream = resourceInfo.getStream();
        InputStream istream =
                new BufferedInputStream(resourceInputStream, input);
        exception = copyRange(istream, ostream, range.start, range.end);

        // Clean up the input stream
        try {
            istream.close();
        } catch (Throwable t) {
            ;
        }

        // Rethrow any exception that has occurred
        if (exception != null)
            throw exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param resourceInfo The ResourceInfo object
     * @param writer The writer to write to
     * @param range Range the client wanted to retrieve
     * @exception IOException if an input/output error occurs
     */
    private void copy(ResourceInfo resourceInfo, PrintWriter writer,
                      Range range)
            throws IOException {

        IOException exception = null;

        InputStream resourceInputStream = resourceInfo.getStream();
        Reader reader = new InputStreamReader(resourceInputStream);
        exception = copyRange(reader, writer, range.start, range.end);

        // Clean up the input stream
        try {
            reader.close();
        } catch (Throwable t) {
            ;
        }

        // Rethrow any exception that has occurred
        if (exception != null)
            throw exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param resourceInfo The ResourceInfo object
     * @param ostream The output stream to write to
     * @param ranges Enumeration of the ranges the client wanted to retrieve
     * @param contentType Content type of the resource
     * @exception IOException if an input/output error occurs
     */
    private void copy(ResourceInfo resourceInfo, ServletOutputStream ostream,
                      Enumeration ranges, String contentType)
            throws IOException {

        IOException exception = null;

        while ( (exception == null) && (ranges.hasMoreElements()) ) {

            InputStream resourceInputStream = resourceInfo.getStream();
            InputStream istream =       // FIXME: internationalization???????
                    new BufferedInputStream(resourceInputStream, input);

            Range currentRange = (Range) ranges.nextElement();

            // Writing MIME header.
            ostream.println("--" + mimeSeparation);
            if (contentType != null)
                ostream.println("Content-Type: " + contentType);
            ostream.println("Content-Range: bytes " + currentRange.start
                    + "-" + currentRange.end + "/"
                    + currentRange.length);
            ostream.println();

            // Printing content
            exception = copyRange(istream, ostream, currentRange.start,
                    currentRange.end);

            try {
                istream.close();
            } catch (Throwable t) {
                ;
            }

        }

        ostream.print("--" + mimeSeparation + "--");

        // Rethrow any exception that has occurred
        if (exception != null)
            throw exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param resourceInfo The ResourceInfo object
     * @param writer The writer to write to
     * @param ranges Enumeration of the ranges the client wanted to retrieve
     * @param contentType Content type of the resource
     * @exception IOException if an input/output error occurs
     */
    private void copy(ResourceInfo resourceInfo, PrintWriter writer,
                      Enumeration ranges, String contentType)
            throws IOException {

        IOException exception = null;

        while ( (exception == null) && (ranges.hasMoreElements()) ) {

            InputStream resourceInputStream = resourceInfo.getStream();
            Reader reader = new InputStreamReader(resourceInputStream);

            Range currentRange = (Range) ranges.nextElement();

            // Writing MIME header.
            writer.println("--" + mimeSeparation);
            if (contentType != null)
                writer.println("Content-Type: " + contentType);
            writer.println("Content-Range: bytes " + currentRange.start
                    + "-" + currentRange.end + "/"
                    + currentRange.length);
            writer.println();

            // Printing content
            exception = copyRange(reader, writer, currentRange.start,
                    currentRange.end);

            try {
                reader.close();
            } catch (Throwable t) {
                ;
            }

        }

        writer.print("--" + mimeSeparation + "--");

        // Rethrow any exception that has occurred
        if (exception != null)
            throw exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param istream The input stream to read from
     * @param ostream The output stream to write to
     * @return Exception which occurred during processing
     */
    private IOException copyRange(InputStream istream,
                                  ServletOutputStream ostream) {

        // Copy the input stream to the output stream
        IOException exception = null;
        byte buffer[] = new byte[input];
        int len = buffer.length;
        while (true) {
            try {
                len = istream.read(buffer);
                if (len == -1)
                    break;
                ostream.write(buffer, 0, len);
            } catch (IOException e) {
                exception = e;
                len = -1;
                break;
            }
        }
        return exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param reader The reader to read from
     * @param writer The writer to write to
     * @return Exception which occurred during processing
     */
    private IOException copyRange(Reader reader, PrintWriter writer) {

        // Copy the input stream to the output stream
        IOException exception = null;
        char buffer[] = new char[input];
        int len = buffer.length;
        while (true) {
            try {
                len = reader.read(buffer);
                if (len == -1)
                    break;
                writer.write(buffer, 0, len);
            } catch (IOException e) {
                exception = e;
                len = -1;
                break;
            }
        }
        return exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param istream The input stream to read from
     * @param ostream The output stream to write to
     * @param start Start of the range which will be copied
     * @param end End of the range which will be copied
     * @return Exception which occurred during processing
     */
    private IOException copyRange(InputStream istream,
                                  ServletOutputStream ostream,
                                  long start, long end) {

        try {
            istream.skip(start);
        } catch (IOException e) {
            return e;
        }

        IOException exception = null;
        long bytesToRead = end - start + 1;

        byte buffer[] = new byte[input];
        int len = buffer.length;
        while ( (bytesToRead > 0) && (len >= buffer.length)) {
            try {
                len = istream.read(buffer);
                if (bytesToRead >= len) {
                    ostream.write(buffer, 0, len);
                    bytesToRead -= len;
                } else {
                    ostream.write(buffer, 0, (int) bytesToRead);
                    bytesToRead = 0;
                }
            } catch (IOException e) {
                exception = e;
                len = -1;
            }
            if (len < buffer.length)
                break;
        }

        return exception;

    }


    /**
     * Copy the contents of the specified input stream to the specified
     * output stream, and ensure that both streams are closed before returning
     * (even in the face of an exception).
     *
     * @param reader The reader to read from
     * @param writer The writer to write to
     * @param start Start of the range which will be copied
     * @param end End of the range which will be copied
     * @return Exception which occurred during processing
     */
    private IOException copyRange(Reader reader, PrintWriter writer,
                                  long start, long end) {

        try {
            reader.skip(start);
        } catch (IOException e) {
            return e;
        }

        IOException exception = null;
        long bytesToRead = end - start + 1;

        char buffer[] = new char[input];
        int len = buffer.length;
        while ( (bytesToRead > 0) && (len >= buffer.length)) {
            try {
                len = reader.read(buffer);
                if (bytesToRead >= len) {
                    writer.write(buffer, 0, len);
                    bytesToRead -= len;
                } else {
                    writer.write(buffer, 0, (int) bytesToRead);
                    bytesToRead = 0;
                }
            } catch (IOException e) {
                exception = e;
                len = -1;
            }
            if (len < buffer.length)
                break;
        }

        return exception;

    }


    /**
     * URL rewriter.
     *
     * @param path Path which has to be rewiten
     */
    protected String rewriteUrl(String path) {
        return urlEncoder.encode( path );
    }

    /**
     * Return a context-relative path, beginning with a "/", that represents
     * the canonical version of the specified path after ".." and "." elements
     * are resolved out.  If the specified path attempts to go outside the
     * boundaries of the current context (i.e. too many ".." path elements
     * are present), return <code>null</code> instead.
     *
     * @param path Path to be normalized
     */
    protected String normalize(String path) {

        if (path == null)
            return null;

        // Create a place for the normalized path
        String normalized = path;

        /*
         * Commented out -- already URL-decoded in StandardContextMapper
         * Decoding twice leaves the container vulnerable to %25 --> '%'
         * attacks.
         *
         * if (normalized.indexOf('%') >= 0)
         *     normalized = RequestUtil.URLDecode(normalized, "UTF8");
         */

        if (normalized == null)
            return (null);

        if (normalized.equals("/."))
            return "/";

        // Normalize the slashes and add leading slash if necessary
        if (normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // Return the normalized path that we have completed
        return (normalized);

    }


    /**
     * Check if the if-match condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resourceInfo File object
     * @return boolean true if the resource meets the specified condition,
     * and false if the condition is not satisfied, in which case request
     * processing is stopped
     */
    private boolean checkIfMatch(HttpServletRequest request,
                                 HttpServletResponse response,
                                 ResourceInfo resourceInfo)
            throws IOException {

        String eTag = getETag(resourceInfo);
        String headerValue = request.getHeader("If-Match");
        if (headerValue != null) {
            if (headerValue.indexOf('*') == -1) {

                StringTokenizer commaTokenizer = new StringTokenizer
                        (headerValue, ",");
                boolean conditionSatisfied = false;

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(eTag))
                        conditionSatisfied = true;
                }

                // If none of the given ETags match, 412 Precodition failed is
                // sent back
                if (!conditionSatisfied) {
                    response.sendError
                            (HttpServletResponse.SC_PRECONDITION_FAILED);
                    return false;
                }

            }
        }
        return true;

    }


    /**
     * Check if the if-modified-since condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resourceInfo File object
     * @return boolean true if the resource meets the specified condition,
     * and false if the condition is not satisfied, in which case request
     * processing is stopped
     */
    private boolean checkIfModifiedSince(HttpServletRequest request,
                                         HttpServletResponse response,
                                         ResourceInfo resourceInfo)
            throws IOException {
        try {
            long headerValue = request.getDateHeader("If-Modified-Since");
            long lastModified = resourceInfo.date;
            if (headerValue != -1) {

                // If an If-None-Match header has been specified, if modified since
                // is ignored.
                if ((request.getHeader("If-None-Match") == null)
                        && (lastModified <= headerValue + 1000)) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return false;
                }
            }
        } catch(IllegalArgumentException illegalArgument) {
            return false;
        }
        return true;

    }


    /**
     * Check if the if-none-match condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resourceInfo File object
     * @return boolean true if the resource meets the specified condition,
     * and false if the condition is not satisfied, in which case request
     * processing is stopped
     */
    private boolean checkIfNoneMatch(HttpServletRequest request,
                                     HttpServletResponse response,
                                     ResourceInfo resourceInfo)
            throws IOException {

        String eTag = getETag(resourceInfo);
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {

            boolean conditionSatisfied = false;

            if (!headerValue.equals("*")) {

                StringTokenizer commaTokenizer =
                        new StringTokenizer(headerValue, ",");

                while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(eTag))
                        conditionSatisfied = true;
                }

            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {

                // For GET and HEAD, we should respond with
                // 304 Not Modified.
                // For every other method, 412 Precondition Failed is sent
                // back.
                if ( ("GET".equals(request.getMethod()))
                        || ("HEAD".equals(request.getMethod())) ) {
                    response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                    return false;
                } else {
                    response.sendError
                            (HttpServletResponse.SC_PRECONDITION_FAILED);
                    return false;
                }
            }
        }
        return true;

    }


    /**
     * Check if the if-unmodified-since condition is satisfied.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param resourceInfo File object
     * @return boolean true if the resource meets the specified condition,
     * and false if the condition is not satisfied, in which case request
     * processing is stopped
     */
    private boolean checkIfUnmodifiedSince(HttpServletRequest request,
                                           HttpServletResponse response,
                                           ResourceInfo resourceInfo)
            throws IOException {
        try {
            long lastModified = resourceInfo.date;
            long headerValue = request.getDateHeader("If-Unmodified-Since");
            if (headerValue != -1) {
                if ( lastModified > headerValue ) {
                    // The entity has not been modified since the date
                    // specified by the client. This is not an error case.
                    response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
                    return false;
                }
            }
        } catch(IllegalArgumentException illegalArgument) {
            return false;
        }
        return true;

    }



    protected class ResourceInfo {

        public Object object;
        public DirContext directory;
        public Resource file;
        public Attributes attributes;
        public String path;
        public long creationDate;
        public String httpDate;
        public long date;
        public long length;
        public boolean collection;
        public String weakETag;
        public String strongETag;
        public boolean exists;
        public DirContext resources;
        protected InputStream is;

        public ResourceInfo(String path, DirContext resources) {
            set(path, resources);
        }

        public void recycle() {
            object = null;
            directory = null;
            file = null;
            attributes = null;
            path = null;
            creationDate = 0;
            httpDate = null;
            date = 0;
            length = -1;
            collection = true;
            weakETag = null;
            strongETag = null;
            exists = false;
            resources = null;
            is = null;
        }


        public void set(String path, DirContext resources) {

            recycle();

            this.path = path;
            this.resources = resources;
            exists = true;
            try {
                object = resources.lookup(path);
                if (object instanceof Resource) {
                    file = (Resource) object;
                    collection = false;
                } else if (object instanceof DirContext) {
                    directory = (DirContext) object;
                    collection = true;
                } else {
                    // Don't know how to serve another object type
                    exists = false;
                }
            } catch (NamingException e) {
                exists = false;
            }
            if (exists) {
                try {
                    attributes = resources.getAttributes(path);
                    if (attributes instanceof ResourceAttributes) {
                        ResourceAttributes tempAttrs =
                                (ResourceAttributes) attributes;
                        Date tempDate = tempAttrs.getCreationDate();
                        if (tempDate != null)
                            creationDate = tempDate.getTime();
                        tempDate = tempAttrs.getLastModifiedDate();
                        if (tempDate != null) {
                            httpDate = FastHttpDateFormat.getDate(tempDate);
                            date = tempDate.getTime();
                        } else {
                            httpDate = FastHttpDateFormat.getCurrentDate();
                        }
                        weakETag = tempAttrs.getETag();
                        strongETag = tempAttrs.getETag(true);
                        length = tempAttrs.getContentLength();
                    }
                } catch (NamingException e) {
                    // Shouldn't happen, the implementation of the DirContext
                    // is probably broken
                    exists = false;
                }
            }

        }


        /**
         * Test if the associated resource exists.
         */
        public boolean exists() {
            return exists;
        }


        /**
         * String representation.
         */
        public String toString() {
            return path;
        }


        /**
         * Set IS.
         */
        public void setStream(InputStream is) {
            this.is = is;
        }


        /**
         * Get IS from resource.
         */
        public InputStream getStream()
                throws IOException {
            if (is != null)
                return is;
            if (file != null)
                return (file.streamContent());
            else
                return null;
        }
    }


    private class Range {
        public long start;
        public long end;
        public long length;

        /**
         * Validate range.
         */
        public boolean validate() {
            if (end >= length)
                end = length - 1;
            return ( (start >= 0) && (end >= 0) && (start <= end)
                    && (length > 0) );
        }

        public void recycle() {
            start = 0;
            end = 0;
            length = 0;
        }
    }
}
