package org.apache.catalina.deploy;

import org.apache.catalina.Context;

import javax.servlet.SessionCookieConfig;
import java.util.Map;

/**
 * Created by tisong on 8/18/16.
 */
public class WebXml {

    private SessionConfig sessionConfig = new SessionConfig();

    public void configureContext(Context context) {

        for (ErrorPage errorPage: errorPage.values()) {
            context.addErrorPage(errorPage);
        }

        for (FilterDef filterDef : filters.values()) {
            context.addFilterDef(filter);
        }

        for (FilterMap filterMap : filterMaps) {
            context.addFilterMap(filterMap);
        }

        for (JspPropertyGroup jspPropertyGroup : jspPropertyGroups) {

        }

        for (String listener : listeners) {

        }

        for (Map.Entry<String, String> entry : mimeMappings.entrySet()) {
            context.addMimeMapping(entry.getKey(), entry.getValue());
        }

        for (ServletDef servlet : servlets.values()) {

        }

        for (Map.Entry<String, String> entry : servletMappings.entrySet()) {
            context.addServletMapping(entry.getKey(), entry.getValue());
        }

        if (sessionConfig != null) {

            SessionCookieConfig scc = context.getSevletContext().getSessionCookieConfig();


        }

        for (Map.Entry<String, String> entry : taglibs.entrySet()) {

        }

        for (String welcomeFile : welcomeFiles) {

        }

        for (JspPropertyGroup jspPropertyGroup : jspPropertyGroups) {

        }

        for (Map.Entry<String, String> entry : postConstructMethods.entrySet()) {

        }

        for (Map.Entry<String, String> entry : preDestoryMethods.entrySet()) {

        }
    }
}
