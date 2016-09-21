package org.apache.naming;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by tisong on 9/20/16.
 */
public class NamingContextEnumeration
    implements NamingEnumeration{


    protected Enumeration enumeration;


    public NamingContextEnumeration(Vector entries) {
        this.enumeration = entries.elements();
    }

    @Override
    public Object next() throws NamingException {
        return null;
    }

    @Override
    public boolean hasMore() throws NamingException {
        return false;
    }

    @Override
    public void close() throws NamingException {

    }

    @Override
    public boolean hasMoreElements() {
        return false;
    }

    @Override
    public Object nextElement() {
        return null;
    }
}
