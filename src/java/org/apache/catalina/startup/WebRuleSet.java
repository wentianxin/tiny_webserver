package org.apache.catalina.startup;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.Rule;
import org.apache.tomcat.util.digester.RuleSetBase;
import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Created by tisong on 9/9/16.
 */
public class WebRuleSet extends RuleSetBase{

    private String prefix = null;

    @Override
    public void addRuleInstances(Digester digester) {

        /** web-app **/

        digester.addRule(prefix + "web-app", new SetPublicIdRule(digester, "setPublicId"));


        /**
         *  web-app/context-param
         *  web-app/context-param/param-name
         *  web-app/context-param/param-value
         **/
        digester.addCallMethod(prefix + "web-app/context-param", "addParameter", 2);
        digester.addCallParam(prefix + "web-app/context-param/param-name", 0);
        digester.addCallParam(prefix + "web-app/context-param/param-value", 1);

        /** web-app/display-name **/

        /** web-app/distributable **/

        /** web-app/ejb-local-ref **/

        /** web-app/ejb-local-ref/description **/


        /** web-app/ejb-local-ref/ejb-link **/

        /** web-app/ejb-local-ref/ejb-ref-name **/

        /** web-app/ejb-local-ref/ejb-ref-type **/

        /** web-app/ejb-local-ref/local **/

        /** web-app/ejb-local-ref/local-home **/


        /** web-app/ejb-ref **/

//        web-app/ejb-ref/description
//        web-app/ejb-ref/ejb-link
//        web-app/ejb-ref/ejb-ref-name
//        web-app/ejb-ref/ejb-ref-type
//        web-app/ejb-ref/home
//        web-app/ejb-ref/remote
//
//
//
//        web-app/env-entry
//        web-app/env-entry/description
//        web-app/env-entry/env-entry-name
//        web-app/env-entry/env-entry-type
//        web-app/env-entry/env-entry-value
//
//
//        web-app/error-page
//        web-app/error-page/error-code
//        web-app/error-page/exception-type
//        web-app/error-page/location
//
//
//        web-app/filter
//        web-app/filter/description
//        web-app/filter/display-name
//        web-app/filter/filter-class
//        web-app/filter/filter-name
//        web-app/filter/large-icon
//        web-app/filter/small-icon
//
//        web-app/filter/init-param
//        web-app/filter/init-param/param-name
//        web-app/filter/init-param/param-value
//
//
//        web-app/filter-mapping
//        web-app/filter-mapping/filter-name
//        web-app/filter-mapping/servlet-name
//        web-app/filter-mapping/url-pattern
//
//
//        web-app/listener/listener-class
//
//
//        web-app/login-config
//        web-app/login-config/auth-method
//        web-app/login-config/realm-name
//        web-app/login-config/form-login-config/form-error-page
//        web-app/login-config/form-login-config/form-login-page
//
//
//        web-app/mime-mapping
//        web-app/mime-mapping/extension
//        web-app/mime-mapping/mime-type
//
//
//        web-app/resource-env-ref
//        web-app/resource-env-ref/resource-env-ref-name
//        web-app/resource-env-ref/resource-env-ref-type
//
//
//        web-app/resource-ref
//        web-app/resource-ref/description
//        web-app/resource-ref/res-auth
//        web-app/resource-ref/res-ref-name
//        web-app/resource-ref/res-sharing-scope
//        web-app/resource-ref/res-type
//
//
//
//
//        web-app/security-constraint
//        web-app/security-constraint/auth-constraint
//        web-app/security-constraint/auth-constraint/role-name
//        web-app/security-constraint/display-name
//        web-app/security-constraint/user-data-constraint/transport-guarantee
//        web-app/security-constraint/web-resource-collection
//        web-app/security-constraint/web-resource-collection/http-method
//        web-app/security-constraint/web-resource-collection/url-pattern
//        web-app/security-constraint/web-resource-collection/web-resource-name
//
//
//
//        web-app/security-role/role-name
//
//
//        web-app/servlet
//        web-app/servlet/init-param
//        web-app/servlet/init-param/param-name
//        web-app/servlet/init-param/param-value

        digester.addRule(prefix + "web-app/servlet", new WrapperCreateRule(digester));
        digester.addSetNext(prefix + "web-app/servlet", "addChild", "org.apache.catalina.Container");


        digester.addCallMethod(prefix + "web-app/servlet/init-param", "addInitParameter", 2);
        digester.addCallParam(prefix + "web-app/servlet/init-param/param-name", 0);
        digester.addCallParam(prefix + "web-app/servlet/init-param/param-value", 1);
//
//
//        web-app/servlet/jsp-file
//        web-app/servlet/load-on-startup
//        web-app/servlet/run-as/role-name
//
//
//        web-app/servlet/security-role-ref
//        web-app/servlet/security-role-ref/role-link
//        web-app/servlet/security-role-ref/role-name
//
//
//
//        web-app/servlet/servlet-class
//        web-app/servlet/servlet-name
//        web-app/servlet-mapping
//        web-app/servlet-mapping/servlet-name
//        web-app/servlet-mapping/url-pattern
//
        digester.addCallMethod(prefix + "web-app/servlet/servlet-class", "setServletClass", 0);
        digester.addCallMethod(prefix + "web-app/servlet/servlet-name", "setName", 0);

        digester.addCallMethod(prefix + "web-app/servlet-mapping", "addServletMapping", 2);
        digester.addCallParam(prefix + "web-app/servlet-mapping/servlet-name", 1);
        digester.addCallParam(prefix + "web-app/servlet-mapping/url-pattern", 0);

        digester.addCallMethod(prefix + "web-app/session-config/session-timeout",
                "setSessionTimeout", 1,
                new Class[] { Integer.TYPE });

        digester.addCallParam(prefix + "web-app/session-config/session-timeout", 0);
//
//        web-app/session-config/session-timeout
//
//
//
//        web-app/taglib
//        web-app/taglib/taglib-location
//        web-app/taglib/taglib-uri
//
//
//
//        web-app/welcome-file-list/welcome-file
    }
}


final class SetPublicIdRule extends Rule {

    private String method = null;

    public SetPublicIdRule(Digester digester, String method) {

        super(digester);
        this.method = method;
    }


    @Override
    public void begin(Attributes attributes) throws IOException {

        /**
         * 调用的是 WebXML 对象
         */
        digester.peek();


    }
}

final class WrapperCreateRule extends Rule {
    public WrapperCreateRule(Digester digester) {
        super(digester);
    }

    public void begin(Attributes attributes) throws Exception {
        Context context =
                (Context) digester.peek(digester.getCount() - 1);
        Wrapper wrapper = context.createWrapper();
        digester.push(wrapper);
    }

    public void end() throws Exception {
        Wrapper wrapper = (Wrapper) digester.pop();
    }
}
