package org.apache.catalina.startup;

import org.apache.catalina.Container;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.Rule;
import org.xml.sax.Attributes;

import java.lang.reflect.Method;

/**
 * Created by tisong on 9/15/16.
 */
public class CopyParentClassLoaderRule extends Rule {


    public CopyParentClassLoaderRule(Digester digester) {
        super(digester);
    }


    public void begin(Attributes attributes) throws Exception {

        Container child = (Container)digester.peek(0); // 栈顶元素

        Container parent = (Container)digester.peek(1);

        Method method = parent.getClass().getMethod("getParentClassLoader", new Class[0]);

        ClassLoader classLoader = (ClassLoader) method.invoke(parent, new Object[0]);

        child.setParentClassLoader(classLoader);
    }
}
