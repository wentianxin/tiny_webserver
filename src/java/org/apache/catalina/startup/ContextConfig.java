package org.apache.catalina.startup;

import org.apache.catalina.*;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tisong on 9/9/16.
 */
public class ContextConfig implements LifecycleListener{

    private Context context = null;

    private boolean ok = false;

    private static Digester webDigester = createWebDigester();



    @Override
    public void lifecycleEvent(LifecycleEvent event) {

        context = (Context)event.getLifecycle();

        if (event.getType().equals(Lifecycle.START_EVENT)) {
            start();
        } else if (event.getType().equals(Lifecycle.STOP_EVENT)) {
            stop();
        }
    }


    private void start() {

        context.setConfigured(false);

        ok = true;


        Container container = context.getParent();
        /**
         * 根据默认Context配置来给当前Context设置恰当的属性(DefaultContext)
         */
        if (!context.getOverride()) {
            if (container instanceof Host) {
                ((Host)container).importDefaultContext(context);
                container = container.getParent();
            }
            if( container instanceof Engine ) {
                ((Engine)container).importDefaultContext(context);
            }
        }

        /**
         * 处理并解析 web.xml 文件
         */

        defaultConfig();

        applicationConfig();

        // TODO 根据是否配置 SSL 安全模式来在 Context 的流水线上加上 CertificatesValvle

        // TODO authenticatorConfig 流水线的配置


        if (ok) {
            context.setConfigured(true);
        } else {
            context.setConfigured(false);
        }
    }

    public void stop() {

    }

    /**
     * conf/web.xml
     */
    private void defaultConfig() {

    }

    /**
     * WEB-INF/web.xml
     */
    private void applicationConfig() {

        /**
         * 只是检测该 web.xml 是否存在
         */
        InputStream stream = null;
        ServletContext servletContext = context.getServletContext();
        if (servletContext != null) {
            // TODO 是如何获取 resource stream 的
            stream = servletContext.getResourceAsStream(Constants.ApplicationWebXml);
        }
        if (stream == null) {
            return;
        }


        try {
            // TODO 关于 URL - InputSource - InputStream 之间的关系
            URL url =  servletContext.getResource(Constants.ApplicationWebXml);
            InputSource is = new InputSource(url.toExternalForm());

            is.setByteStream(stream);

            webDigester.clear();
            webDigester.push(context);
            webDigester.parse(is);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            ok = false;
        } catch (SAXException e) {
            e.printStackTrace();
            ok = false;
        } catch (IOException e) {
            e.printStackTrace();
            ok = false;
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


    private static Digester createWebDigester() {

        Digester digester = new Digester();

        digester.setValidating(true);

        URL url = null;

        url = ContextConfig.class.getResource(Constants.WebDtdResourcePath_22);

        digester.register(Constants.WebDtdPublicId_22, url.toString());

        url = ContextConfig.class.getResource(Constants.WebDtdResourcePath_23);

        digester.register(Constants.WebDtdPublicId_23, url.toString());

        digester.addRuleSet(new WebRuleSet());


        return  digester;
    }
}


