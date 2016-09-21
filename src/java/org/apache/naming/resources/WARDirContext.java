package org.apache.naming.resources;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

/**
 * Created by tisong on 9/15/16.
 */
public class WARDirContext extends BaseDirContext {
    @Override
    public Attributes getAttributes(String name, String[] attrIds) throws NamingException {
        return null;
    }

    @Override
    public void modifyAttributes(String name, int mod_op, Attributes attrs) throws NamingException {

    }

    @Override
    public void modifyAttributes(String name, ModificationItem[] mods) throws NamingException {

    }

    @Override
    public void bind(String name, Object obj, Attributes attrs) throws NamingException {

    }

    @Override
    public void rebind(String name, Object obj, Attributes attrs) throws NamingException {

    }

    @Override
    public DirContext createSubcontext(String name, Attributes attrs) throws NamingException {
        return null;
    }

    @Override
    public DirContext getSchema(String name) throws NamingException {
        return null;
    }

    @Override
    public Object lookup(String name) throws NamingException {
        return null;
    }
}
