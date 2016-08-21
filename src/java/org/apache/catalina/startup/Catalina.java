package org.apache.catalina.startup;

import org.apache.catalina.Globals;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.tomcat.util.digester.Digester;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tisong on 8/16/16.
 */
public class Catalina {

    private boolean useNaming = true;

    private String configFile = "conf/server.xml";

    private Server server = null;

    public void load() {

        // TODO
        initDirs();
        initNaming();

        Digester digester = createStartDigester();

        InputStream inputStream = null;
        InputSource inputSource = null;
        File file = null;
        try {
            file = configFile();
            inputStream = new FileInputStream(file);
            inputSource = new InputSource(file.toURI().toURL().toString());
        }  catch (Exception e) {

        }



        try {
            inputSource.setByteStream(inputStream);
            digester.push(this);
            digester.parse(inputSource);
        } catch (Exception e) {

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                // Ignore
            }
        }

        getServer().setCatalina(this);

        // Stream redirection
        initStreams();

        try {
            getServer().init();
        } catch (LifecycleException e) {

        }


    }

    public void start() {

        if (getServer() == null) {
            // 方法隔离性
            load();
        }

        if (getServer() == null) {

            return;
        }


        try {
            getServer().start();
        } catch (LifecycleException e) {

        }

        if (useShutdownHook) {
            if (shutdownHook == null) {
                shutdownHook = new CatalinaShutdownHook();
            }
            Runtime.getRuntime().addShutdownHook(shutdownHook);

            // TODO

        }

        if (await) {
            await();
            stop();
        }
    }


    private Digester createStartDigester() {


        Digester digester = new Digester();


        digester.addObjectCreate("Server",
                                "org.apache.catalina.core.StandardServer",
                                "className");
        digester.addSetProperties("Server");
        digester.addSetNext("Server",
                            "setServer",
                            "org.apache.catalina.Server");

        digester.addObjectCreate("Server/GlobalNamingResources",
                                 "org.apache.catalina.deploy.NamingResources");
        digester.addSetProperties("Server/GlobalNamingResources");
        digester.addSetNext("Server/GlobalNamingResources",
                "setGlobalNamingResources",
                "org.apache.catalina.deploy.NamingResources");

        digester.addObjectCreate("Server/Listener",
                                 null,
                                 "className");
        digester.addSetProperties("Server/Listener");
        digester.addSetNext("Server/Service/Listener",
                "addLifecycleListener",
                "org.apache.catalina.LifecycleListener");



        return digester;
    }


    protected void initDirs() {

    }

    private void initNaming() {
        if (!useNaming) {

        } else {

        }
    }

    protected void initStreams() {

    }

    private File configFile() {
        File file = new File(configFile);
        if (!file.isAbsolute()) {
            // 如果file不是绝对路径
            file = new File(System.getProperty(Globals.CATALINA_HOME_PROP), configFile);
        }

        return file;
    }

    public Server getServer() {
        return this.server;
    }
}
