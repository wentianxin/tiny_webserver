package org.apache.catalina.util;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by tisong on 9/20/16.
 */
public class Enumerator implements Enumeration {


    private Iterator iterator = null;


    public Enumerator(Collection collection) {

        this(collection.iterator());

    }

    public Enumerator(Iterator iterator) {

        this.iterator = iterator;
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
