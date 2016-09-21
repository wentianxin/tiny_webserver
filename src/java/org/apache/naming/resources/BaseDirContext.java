package org.apache.naming.resources;

import javax.naming.*;
import javax.naming.directory.*;
import java.util.Hashtable;

/**
 * Created by tisong on 9/15/16.
 */
public abstract class BaseDirContext implements DirContext {


    protected String docBase = null;

    protected Hashtable env = null;

    protected boolean cached = true;



    public BaseDirContext() {
        this.env = new Hashtable();
    }

    public BaseDirContext(Hashtable env) {
        this.env = env;
    }



    public void setDocBase(String docBase) {
        this.docBase = docBase;
    }

    public String getDocBase() {
        return docBase;
    }


    public void setCached(boolean cached) {
        this.cached = cached;
    }

    public boolean isCached() {
        return cached;
    }



    @Override
    public Attributes getAttributes(Name name) throws NamingException {
        return getAttributes(name.toString());
    }

    @Override
    public Attributes getAttributes(String name) throws NamingException {
        return getAttributes(name, null);
    }

    @Override
    public Attributes getAttributes(Name name, String[] attrIds) throws NamingException {
        return getAttributes(name.toString(), attrIds);
    }

    @Override
    public abstract Attributes getAttributes(String name, String[] attrIds) throws NamingException;



    @Override
    public void modifyAttributes(Name name, int mod_op, Attributes attrs) throws NamingException {
        modifyAttributes(name.toString(), mod_op, attrs);
    }

    @Override
    public abstract void modifyAttributes(String name, int mod_op, Attributes attrs) throws NamingException;


    @Override
    public void modifyAttributes(Name name, ModificationItem[] mods) throws NamingException {
        modifyAttributes(name.toString(), mods);
    }

    @Override
    public abstract void modifyAttributes(String name, ModificationItem[] mods) throws NamingException;

    @Override
    public void bind(Name name, Object obj, Attributes attrs) throws NamingException {
        bind(name.toString(), obj, attrs);
    }

    @Override
    public abstract void bind(String name, Object obj, Attributes attrs) throws NamingException;

    @Override
    public void rebind(Name name, Object obj, Attributes attrs) throws NamingException {
        rebind(name.toString(), obj, attrs);
    }

    @Override
    public abstract void rebind(String name, Object obj, Attributes attrs) throws NamingException;

    @Override
    public DirContext createSubcontext(Name name, Attributes attrs) throws NamingException {
        return createSubcontext(name.toString(), attrs);
    }

    @Override
    public abstract DirContext createSubcontext(String name, Attributes attrs) throws NamingException;

    @Override
    public DirContext getSchema(Name name) throws NamingException {
        return getSchema(name.toString());
    }

    @Override
    public abstract DirContext getSchema(String name) throws NamingException;

    @Override
    public DirContext getSchemaClassDefinition(Name name) throws NamingException {
        return null;
    }

    @Override
    public DirContext getSchemaClassDefinition(String name) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> search(Name name, Attributes matchingAttributes, String[] attributesToReturn) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, Attributes matchingAttributes, String[] attributesToReturn) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> search(Name name, Attributes matchingAttributes) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, Attributes matchingAttributes) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> search(Name name, String filter, SearchControls cons) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, String filter, SearchControls cons) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> search(Name name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<SearchResult> search(String name, String filterExpr, Object[] filterArgs, SearchControls cons) throws NamingException {
        return null;
    }

    @Override
    public Object lookup(Name name) throws NamingException {
        return lookup(name.toString());
    }

    @Override
    public abstract Object lookup(String name) throws NamingException;




    @Override
    public void bind(Name name, Object obj) throws NamingException {
        bind(name.toString(), obj);
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        bind(name, obj, null);
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {

    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {

    }

    @Override
    public void unbind(Name name) throws NamingException {

    }

    @Override
    public void unbind(String name) throws NamingException {

    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {

    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {

    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return null;
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        return null;
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {

    }

    @Override
    public void destroySubcontext(String name) throws NamingException {

    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        return null;
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return null;
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        return null;
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return null;
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        return null;
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        return null;
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {
        return null;
    }

    @Override
    public String composeName(String name, String prefix) throws NamingException {
        return null;
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return null;
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        return null;
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return null;
    }

    @Override
    public void close() throws NamingException {

    }

    @Override
    public String getNameInNamespace() throws NamingException {
        return null;
    }
}
