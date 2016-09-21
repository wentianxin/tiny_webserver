package org.apache.catalina.core;

import jdk.internal.util.xml.impl.Input;
import org.apache.catalina.*;
import org.apache.catalina.startup.*;
import org.apache.catalina.startup.Constants;
import org.apache.tomcat.util.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tisong on 9/9/16.
 */
public class ContextConfig implements LifecycleListener{


    private Context context = null;

    private Digester webDigester = createWebDigester();

    @Override
    public void lifecycleEvent(LifecycleEvent event) {

        context = (Context) event.getLifecycle();

        if (event.getType().equals(Lifecycle.START_EVENT)) {
            start();
        } else if (event.getType().equals(Lifecycle.STOP_EVENT)) {
            stop();
        }
    }


    private void start() {

        context.setConfigured(false);

        boolean ok = true;

        Container container = context.getParent();
        if ( !context.getOverride() ) {

        }

        defaultConfig();
        applicationConfig();

        if (ok) {
            context.setConfigured(true);
        } else {
            context.setConfigured(false);
        }
    }

    private void stop() {

    }

    private void defaultConfig() {

        File file = new File(Constants.DefaultWebXml);
        if (!file.isAbsolute()) {
            file = new File(System.getProperty("catalina.base"), Constants.DefaultWebXml);
        }

        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file.getCanonicalFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return ;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        InputSource source = null;
        try {

            source = new InputSource("file://" + file.getAbsolutePath());
            stream = new FileInputStream(file);

            source.setByteStream(stream);

            webDigester.clear();
            webDigester.push(this);
            webDigester.parse(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void applicationConfig() {

        InputStream stream = null;

        ServletContext servletContext = context.getServletContext();
        if (servletContext != null) {
            // TODO
            stream = servletContext.getResourceAsStream(Constants.ApplicationWebXml);
        }

        if (stream == null) {
            return ;
        }

        try {
            URL url = servletContext.getResource(Constants.ApplicationWebXml);

            InputSource source = new InputSource(url.toExternalForm());

            source.setByteStream(stream);
//            webDigester.setDebug(getDebug());
            if (context instanceof StandardContext) {
//                ((StandardContext) context).setReplaceWelcomeFiles(true);
            }
            webDigester.clear();
            webDigester.push(context);
            webDigester.parse(source);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private Digester createWebDigester() {

        Digester digester = new Digester();

        digester.setValidating(true);

        digester.addRuleSet(new WebRuleSet());

        return digester;
    }
}
