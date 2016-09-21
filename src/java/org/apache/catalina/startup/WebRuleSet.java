package org.apache.catalina.startup;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.deploy.SecurityConstraint;
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
        /** web-app/ejb-local-ref/description**/
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


        /**
         * web-app/env-entry
         * web-app/env-entry/description
         * web-app/env-entry/env-entry-name
         * web-app/env-entry/env-entry-type
         * web-app/env-entry/env-entry-value
         */
        digester.addObjectCreate("web-app/env-entry", "org.apache.catalina.deploy.ContextEnvironment", "className");
        digester.addCallMethod("web-app/env-entry/description", "setDescription", 0);
        digester.addCallMethod("web-app/env-entry/env-entry-name", "setName", 0);
        digester.addCallMethod("web-app/env-entry/env-entry-value", "setValue", 0);
        digester.addCallMethod("web-app/env-entry/env-entry-type", "setType", 0);


//        web-app/error-page
//        web-app/error-page/error-code
//        web-app/error-page/exception-type
//        web-app/error-page/location


//        web-app/filter
//        web-app/filter/description
//        web-app/filter/display-name
//        web-app/filter/filter-class
//        web-app/filter/filter-name
//        web-app/filter/large-icon
//        web-app/filter/small-icon
//        web-app/filter/init-param
//        web-app/filter/init-param/param-name
//        web-app/filter/init-param/param-value
//        web-app/filter-mapping
//        web-app/filter-mapping/filter-name
//        web-app/filter-mapping/servlet-name
//        web-app/filter-mapping/url-pattern
        digester.addObjectCreate("web-app/filter",
                                "org.apache.catalina.deploy.FilterDef",
                                "className");
        digester.addSetNext("web-app/filter",
                            "addFilterDef",
                            "org.apache.catalina.deploy.FilterDef");
        digester.addCallMethod("web-app/filter-name", "setFilterName", 0);
        digester.addCallMethod("web-app/filter-class", "setFilterClass", 0);
        digester.addCallMethod("web-app/filter/init-param", "addInitParameter", 2);
        digester.addCallParam("web-app/filter/init-param/param-name", 0);
        digester.addCallParam("web-app/filter/init-param/param-value", 1);


        digester.addObjectCreate("web-app/filter-mapping", "org.apache.catalina.deploy.FilterMap", "className");
        digester.addSetNext("web-app/filter-mapping", "addFilterMap", "org.apache.catalina.deploy.FilterMap");
        digester.addCallMethod("web-app/filter-mapping/filter-name", "setFilterName", 0);
        digester.addCallMethod("web-app/filter-mapping/servlet-name", "setServletName", 0);
        digester.addCallMethod("web-app/filter-mapping/url-pattern", "setUrlPattern", 0);


        /**
         * web-app/listener/listener-class
         */
        digester.addCallMethod("web-app/listener/listener-class", "addApplicationListener", 0);





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


        /**
         * web-app/security-constraint
         * web-app/security-constraint/auth-constraint (auth-constraint元素却指出哪些用户应该具有受保护资源的访问权；)
         * web-app/security-constraint/auth-constraint/role-name (包含一个或多个标识具有访问权限的用户类别role-name元素，以及包含（可选）一个描述角色的description元素)
         * web-app/security-constraint/display-name
         * web-app/security-constraint/user-data-constraint/transport-guarantee
         * web-app/security-constraint/web-resource-collection (web-resource-collection 此元素确定应该保护的资源)
         * web-app/security-constraint/web-resource-collection/http-method (一个指出此保护所适用的HTTP命令（GET、POST等，缺省为所有方法）的http-method元素和一个提供资料的可选description元素组成)
         * web-app/security-constraint/web-resource-collection/url-pattern (一个确定应该保护URL的url-pattern元素、)
         * web-app/security-constraint/web-resource-collection/web-resource-name (此元素由一个给出任意标识名称的web-resource-name元素)
         */
        digester.addObjectCreate(prefix + "web-app/security-constraint",
                "org.apache.catalina.deploy.SecurityConstraint");
        digester.addSetNext(prefix + "web-app/security-constraint",
                "addConstraint",
                "org.apache.catalina.deploy.SecurityConstraint");


        digester.addObjectCreate(prefix + "web-app/security-constraint/web-resource-collection",
                "org.apache.catalina.deploy.SecurityCollection");
        digester.addSetNext(prefix + "web-app/security-constraint/web-resource-collection",
                "addCollection",
                "org.apache.catalina.deploy.SecurityCollection");
        digester.addCallMethod(prefix + "web-app/security-constraint/web-resource-collection/web-resource-name", "setName", 0);
        digester.addCallMethod(prefix + "web-app/security-constraint/web-resource-collection/http-method", "addMethod", 0);
        digester.addCallMethod(prefix + "web-app/security-constraint/web-resource-collection/url-pattern", "addPattern", 0);

        digester.addRule(prefix + "web-app/security-constraint/auth-constraint",
                new SetAuthConstraintRule(digester));
        digester.addCallMethod(prefix + "web-app/security-constraint/auth-constraint/role-name",
                "addAuthRole", 0);


        /**
         * web-app/login-config
         * web-app/login-config/auth-method (四种认证类型：BASIC； DIGEST；CLIENT-CERT; FROM)
         * web-app/login-config/realm-name
         * web-app/login-config/form-login-config/form-error-page
         * web-app/login-config/form-login-config/form-login-page
         */
        digester.addObjectCreate(prefix + "web-app/login-config",
                "org.apache.catalina.deploy.LoginConfig");
        digester.addSetNext(prefix + "web-app/login-config",
                "setLoginConfig",
                "org.apache.catalina.deploy.LoginConfig");
        digester.addCallMethod(prefix + "web-app/login-config/auth-method",
                "setAuthMethod", 0);
        digester.addCallMethod(prefix + "web-app/login-config/realm-name",
                "setRealmName", 0);
        digester.addCallMethod(prefix + "web-app/login-config/form-login-config/form-error-page",
                "setErrorPage", 0);
        digester.addCallMethod(prefix + "web-app/login-config/form-login-config/form-login-page",
                "setLoginPage", 0);


        /**
         * web-app/security-role/role-name
         */
        digester.addCallMethod(prefix + "web-app/security-role/role-name",
                "addSecurityRole", 0);


        /**
         * web-app/servlet
         * web-app/servlet/init-param
         * web-app/servlet/init-param/param-name
         * web-app/servlet/init-param/param-value
         */
        digester.addRule(prefix + "web-app/servlet", new WrapperCreateRule(digester));
        digester.addSetNext(prefix + "web-app/servlet", "addChild", "org.apache.catalina.Container");


        digester.addCallMethod(prefix + "web-app/servlet/init-param", "addInitParameter", 2);
        digester.addCallParam(prefix + "web-app/servlet/init-param/param-name", 0);
        digester.addCallParam(prefix + "web-app/servlet/init-param/param-value", 1);

        /**
         * web-app/servlet/servlet-class
         * web-app/servlet/servlet-name
         * web-app/servlet-mapping
         * web-app/servlet-mapping/servlet-name
         * web-app/servlet-mapping/url-pattern
         */
        digester.addCallMethod(prefix + "web-app/servlet/servlet-class", "setServletClass", 0);
        digester.addCallMethod(prefix + "web-app/servlet/servlet-name", "setName", 0);

        digester.addCallMethod(prefix + "web-app/servlet-mapping", "addServletMapping", 2);
        digester.addCallParam(prefix + "web-app/servlet-mapping/servlet-name", 1);
        digester.addCallParam(prefix + "web-app/servlet-mapping/url-pattern", 0);


        /**
         * web-app/session-config/session-timeout
         */
//        digester.addCallMethod(prefix + "web-app/session-config/session-timeout",
//                "setSessionTimeout", 1,
//                new Class[] { Integer.TYPE });
//        digester.addCallParam(prefix + "web-app/session-config/session-timeout", 0);



//        web-app/servlet/jsp-file
//        web-app/servlet/load-on-startup
//        web-app/servlet/run-as/role-name


//        web-app/servlet/security-role-ref
//        web-app/servlet/security-role-ref/role-link
//        web-app/servlet/security-role-ref/role-name


//        web-app/taglib
//        web-app/taglib/taglib-location
//        web-app/taglib/taglib-uri
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


final class SetAuthConstraintRule extends Rule {

    public SetAuthConstraintRule(Digester digester) {
        super(digester);
    }

    public void begin(Attributes attributes) throws Exception {
        SecurityConstraint securityConstraint =
                (SecurityConstraint) digester.peek();
        securityConstraint.setAuthConstraint(true);
    }

}
