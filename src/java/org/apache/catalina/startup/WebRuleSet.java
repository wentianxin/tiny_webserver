package org.apache.catalina.startup;

import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSetBase;

/**
 * Created by tisong on 8/20/16.
 */
public class WebRuleSet extends RuleSetBase{

    private String prefix = null;

    private String fullPrefix = null;

    public WebRuleSet(String prefix, boolean fragment) {
        super();
        this.namespaceURI = null;
        this.prefix = prefix;
        this.fragment = fragment;

        if(fragment) {
            fullPrefix = prefix + "web-fragment";
        } else {
            fullPrefix = prefix + "web-app";
        }

        absoluteOrdering = new AbsoluteOrderingRule(fragment);
        relativeOrdering = new RelativeOrderingRule(fragment);
    }

    @Override
    public void addRuleInstances(Digester digester) {

    }
}
