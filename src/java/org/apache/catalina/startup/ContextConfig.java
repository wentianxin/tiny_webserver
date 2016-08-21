package org.apache.catalina.startup;

import jdk.internal.util.xml.impl.Input;
import org.apache.catalina.*;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.deploy.WebXml;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.JarScannerCallback;
import org.apache.tomcat.util.digester.Digester;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.naming.Binding;
import javax.naming.NamingEnumeration;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tisong on 8/18/16.
 */
public class ContextConfig implements LifecycleListener {

    private Context context = null;


    protected String defaultWebXml = null;


    protected static final Properties authenticators;

    private static final Set<String> pluggabilityJarsToSkip = new HashSet<String>();


    private static final Map<Host, DefaultWebXmlCacheEntry> hostWebXmlCache = new ConcurrentHashMap<Host, DefaultWebXmlCacheEntry>();
    static {

        addJarsToSkip(Constants.DEFAULT_JARS_TO_SKIP);
        addJarsToSkip(Constants.PLUGGABILITY_JARS_TO_SKIP);
    }


    public String getDefaultWebXml() {
        if (defaultWebXml == null) {
            defaultWebXml = Constants.DefaultWebXml;
        }

        return this.defaultWebXml;
    }
    private static void addJarsToSkip(String systemPropertyName) {

    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {

        context = (Context) event.getLifecycle();

        if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
            configureStart();
        }
    }

    private void configureStart() {

        webConfig();


    }

    private void webConfig() {

        Set<WebXml> defaults = new HashSet<WebXml>();

        defaults.add(getDefaultWebXmlFragment());

        WebXml webXml = createWebXml();

        InputSource contextWebXml = getContextWebXmlSource();
        parseWebXml(contextWebXml, webXml, false);

        ServletContext sContext = context.getServletContext();

        Map<String, WebXml> fragments = processJarsForWebFraments();

        Set<WebXml> orderedFragments = null;

        orderedFragments = WebXml.orderWebFragments(webXml, fragments, sContext);


        if (ok) {
            processServletContainerInitializers(orderedFragments);
        }


        if (ok) {
            NamingEnumeration<Binding> listBindings = null;

            listBindings = context.getResources().listBindings("/WEB-INF/classes");

            while(listBindings != null &&
                    listBindings.hasMoreElements()) {
                Binding binding = listBindings.nextElement();
                if (binding.getObject() instanceof FileDirContext) {
                    File webInfClassDir = new File(
                            ((FileDirContext)binding.getObject()).getDocBase() );
                    processAnnotationsFile(webInfClassDir, webXml, webXml.isMetadataComplete());
                } else {
                    String resource = "/WEB-INF/classes/" + binding.getName();

                    URL url = sContext.getResource(resource);
                    processAnnotationUrl(url, webXml, webXml.isMetadataComplete());

                }
            }
        }

        if (ok) {
            processAnnotations(orderedFragments, webXml.isMetadataComplete());
        }

        if  (!webXml.isMetadataComplete()) {
            if (ok) {
                oko = webXml.merge(orderedFragments);
            }

            webXml.merge(defaults);

            if (ok) {
                converJsps(webXml);
            }

            if (ok) {
                webXml.configureContext(context);
            }
        } else {
            webXml.merge(defaults);
            converJsps(webXml);
            webXml.configureContext(context);
        }
        javaClassCache.clear();

        /* 将配置信息保存起来已供其他组件访问 */
        String mergedWebXml = webXml.toXml();
        sContext.setAttribute(
                org.apache.tomcat.util.scan.Constants.MERGED_WEB_XML,
                mergedWebXml);

        /* 检索jar包中的静态资源 */
        if (ok) {
            Set<WebXml> resourceJars = new LinkedHashSet<WebXml>();

        }


        /* 将ServletContainerInitializer配置到上下文。 */
    }

    private WebXml getDefaultWebXmlFragment() {

        Host host = (Host) context.getParent();

        DefaultWebXmlCacheEntry entry = hostWebXmlCache.get(host);

        InputSource globalWebXml = getGlobalWebXmlSource();
        InputSource hostWebXml = getHostWebXmlSource();

        long globalTimeStamp = 0;
        long hostTimeStamp = 0;

        if (globalWebXml != null) {
            try {
                URL url = new URL(globalWebXml.getSystemId());
                globalTimeStamp = url.openConnection().getLastModified();
            } catch (MalformedURLException e) {
                globalTimeStamp = -1;
            } catch (IOException e) {
                globalTimeStamp = -1;
            }
        }

        if (hostWebXml != null) {
            try {
                URL url = new URL(hostWebXml.getSystemId());
                hostTimeStamp = url.openConnection().getLastModified();
            } catch (MalformedURLException e) {
                hostTimeStamp = -1;
            } catch (IOException e) {
                hostTimeStamp = -1;
            }
        }

        if (entry != null && entry.getGlobalTimeStamp() == globalTimeStamp &&
                entry.getHostTimeStamp() == hostTimeStamp) {
            return entry.getWebXml();
        }


        WebXml webXmlDefaultFragment = createWebXml();

        if (globalWebXml == null) {

        } else {
            parseWebXml(globalWebXml, webXmlDefaultFragment, false);
        }
        parseWebXml(hostWebXml, webXmlDefaultFragment, false);

        if (globalTimeStamp != -1 && hostTimeStamp != -1) {
            entry = new DefaultWebXmlCacheEntry(webXmlDefaultFragment, globalTimeStamp, hostTimeStamp);
            hostWebXmlCache.put(host, entry);
        }

        return webXmlDefaultFragment;
    }

    private InputSource getContextWebXmlSource() {
        ServletContext servletContext = context.getServletContext();

        if (servletContext != null) {


            stream = new FIleInpuutStream(altDDName);
            url = new File(altDDName).toURI().toURL();
        }

        if (stream == null || url == null) {

        } else {
            source = new InputSource(url.toExternalForm());
            source.setByteStream(stream);
        }

        return source;
    }

    protected void parseWebXml(InputSource source, WebXml dest, boolean fragment) {

        Digester digester;
        WebRuleSet ruleSet;

        if (fragment) {
            digester = webFragmentDigester;
            ruleSet = webFragmentRuleSet;
        } else {
            digester = webDigester;
            ruleSet = webRuleSet;
        }

        digester.push(dest);
        digester.setErrorHandler(handler);


        try {
            digester.parse(source);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private Map<String, WebXml> processJarsForWebFragments() {

        JarScanner jarScanner = context.getJarScanner();
        FragmentJarScannerCallback callback = new FragmentJarScannerCallback();

        jarScanner.scan(context.getServletContext(),
                context.getLoader().getClassLoader(), callback,
                pluggabilityJarsToSkip);

        return callback.getFragments();
    }

    private static class DefaultWebXmlCacheEntry {
        private final WebXml webXml;
        private final long globalTimeStamp;
        private final long hostTimeStamp;

        public DefaultWebXmlCacheEntry(WebXml webXml, long globalTimeStamp,
                                       long hostTimeStamp) {
            this.webXml = webXml;
            this.globalTimeStamp = globalTimeStamp;
            this.hostTimeStamp = hostTimeStamp;
        }

        public WebXml getWebXml() {
            return webXml;
        }

        public long getGlobalTimeStamp() {
            return globalTimeStamp;
        }

        public long getHostTimeStamp() {
            return hostTimeStamp;
        }
    }

    private class FramentJarScannerCallback implements JarScannerCallback {

        private static final String FRAGMENT_LOCATION =
                "META_INF/web-fragment.xml";
        private Map<String, WebXml> fragments = new HashMap<String, WebXml>();

        @Override
        public void scan(JarURLConnection jarConn) throws IOException {

            URL url = jarConn.getURL();
            URL resourceURL = jarConn.getJarFileURL();

            Jar jar = null;

            try {


            } finally {

            }
        }

        @Override
        public void scan(File file) throws IOException {

        }
    }
    protected InputSource getGlobalWebXmlSource() {

        if (defaultWebXml == null && context instanceof StandardContext) {
            defaultWebXml = ((StandardContext) context).getDefaultWebXml();
        }

        if (defaultWebXml == null) {
            getDefaultWebXml();
        }



        return getWebXmlSource(defaultWebXml, getBaseDir());
    }

    protected InputSource getHostWebXmlSource() {
        File hostConfigBase = getHostConfigBase();

        return getWebXmlSource(Constants.HostWebXml, hostConfigBase.getPath());
    }


    protected String getBaseDir() {

    }

    protected InputSource getWebXmlSource(String fileName, String path) {

    }
}
