package org.apache.naming.resources;



import javax.naming.*;
import javax.naming.directory.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Created by tisong on 9/19/16.
 */
public class ProxyDirContext implements DirContext{

    public static final String HOST = "host";

    public static final String CONTEXT = "context";


    private Hashtable env = null;


    private DirContext dirContext = null;

    protected String hostName = null;

    protected String contextName = null;

    /**
     *
     */
    protected Hashtable cache = null;

    protected int cacheTTL = 5000;

    protected int cacheObjectMaxSize = 32768;



    public ProxyDirContext(Hashtable env, DirContext dirContext) {

        this.env = env;
        this.dirContext = dirContext;
        if (dirContext instanceof BaseDirContext) {

        }

        hostName = (String) env.get(HOST);
        contextName = (String)env.get(CONTEXT);
    }


    public String getDocBase() {
        if (dirContext instanceof BaseDirContext) {
            return ((BaseDirContext) dirContext).getDocBase();
        } else {
            return "";
        }
    }

    public String getHostName() {
        return hostName;
    }

    public String getContextName() {
        return contextName;
    }



    @Override
    public Attributes getAttributes(Name name) throws NamingException {

        Attributes attributes = dirContext.getAttributes(parseName(name));

        return attributes;
    }



    @Override
    public Attributes getAttributes(String name) throws NamingException {
        Attributes attributes = dirContext.getAttributes(parseName(name));
        if (!(attributes instanceof ResourceAttributes)) {
            attributes = new ResourceAttributes(attributes);
        }
        return attributes;
    }

    @Override
    public Attributes getAttributes(Name name, String[] attrIds) throws NamingException {
        Attributes attributes =
                dirContext.getAttributes(parseName(name), attrIds);
        if (!(attributes instanceof ResourceAttributes)) {
            attributes = new ResourceAttributes(attributes);
        }
        return attributes;
    }

    @Override
    public Attributes getAttributes(String name, String[] attrIds) throws NamingException {
        Attributes attributes =
                dirContext.getAttributes(parseName(name), attrIds);
        if (!(attributes instanceof ResourceAttributes)) {
            attributes = new ResourceAttributes(attributes);
        }
        return attributes;
    }

    @Override
    public void modifyAttributes(Name name, int mod_op, Attributes attrs) throws NamingException {

    }

    @Override
    public void modifyAttributes(String name, int mod_op, Attributes attrs) throws NamingException {

    }

    @Override
    public void modifyAttributes(Name name, ModificationItem[] mods) throws NamingException {

    }

    @Override
    public void modifyAttributes(String name, ModificationItem[] mods) throws NamingException {

    }

    @Override
    public void bind(Name name, Object obj, Attributes attrs) throws NamingException {
        dirContext.bind(parseName(name), obj, attrs);
    }

    @Override
    public void bind(String name, Object obj, Attributes attrs) throws NamingException {
        dirContext.bind(parseName(name), obj, attrs);
    }

    @Override
    public void rebind(Name name, Object obj, Attributes attrs) throws NamingException {
        dirContext.rebind(parseName(name), obj, attrs);
    }

    @Override
    public void rebind(String name, Object obj, Attributes attrs) throws NamingException {
        dirContext.rebind(parseName(name), obj, attrs);
    }

    @Override
    public DirContext createSubcontext(Name name, Attributes attrs) throws NamingException {
        return dirContext.createSubcontext(parseName(name), attrs);
    }

    @Override
    public DirContext createSubcontext(String name, Attributes attrs) throws NamingException {
        return dirContext.createSubcontext(parseName(name), attrs);
    }

    @Override
    public DirContext getSchema(Name name) throws NamingException {
        return dirContext.getSchema(parseName(name));
    }

    @Override
    public DirContext getSchema(String name) throws NamingException {
        return dirContext.getSchema(parseName(name));
    }

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
        return dirContext.search(parseName(name), matchingAttributes,
                attributesToReturn);
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

        CacheEntry entry = cacheLookup(name.toString());

        if (entry != null) {

        }

        Object object = dirContext.lookup(parseName(name));
        if (object instanceof InputStream) {
            System.out.println("proxy dir context look up : InputStream");
            return new Resource( (InputStream) object);
        } else {
            return object;
        }
    }

    @Override
    public Object lookup(String name) throws NamingException {
        CacheEntry entry = cacheLookup(name);
        if (entry != null) {
            if (entry.resource != null) {
                return entry.resource;
            } else {
                return entry.context;
            }
        }
        Object object = dirContext.lookup(parseName(name));
        if (object instanceof InputStream) {
            return new Resource((InputStream) object);
        } else if (object instanceof DirContext) {
            return object;
        } else if (object instanceof Resource) {
            return object;
        } else {
            return new Resource(new ByteArrayInputStream
                    (object.toString().getBytes()));
        }
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {
        dirContext.bind(parseName(name), obj);
        cacheUnload(name.toString());
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        dirContext.bind(parseName(name), obj);
        cacheUnload(name);
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {
        dirContext.rebind(parseName(name), obj);
        cacheUnload(name.toString());
    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {
        dirContext.rebind(parseName(name), obj);
        cacheUnload(name);
    }

    @Override
    public void unbind(Name name) throws NamingException {
        dirContext.unbind(parseName(name));
        cacheUnload(name.toString());
    }

    @Override
    public void unbind(String name) throws NamingException {
        dirContext.unbind(parseName(name));
        cacheUnload(name);
    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {
        dirContext.rename(parseName(oldName), parseName(newName));
        cacheUnload(oldName.toString());
    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {
        dirContext.rename(parseName(oldName), parseName(newName));
        cacheUnload(oldName);
    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return dirContext.list(parseName(name));
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return dirContext.list(parseName(name));
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return dirContext.listBindings(parseName(name));
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        return dirContext.listBindings(parseName(name));
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
        dirContext.destroySubcontext(parseName(name));
        cacheUnload(name.toString());
    }

    @Override
    public void destroySubcontext(String name) throws NamingException {
        dirContext.destroySubcontext(parseName(name));
        cacheUnload(name);
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        return dirContext.createSubcontext(parseName(name));
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return dirContext.createSubcontext(parseName(name));
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        return dirContext.lookupLink(parseName(name));
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return dirContext.lookupLink(parseName(name));
    }

    @Override
    public NameParser getNameParser(Name name)
            throws NamingException {
        return dirContext.getNameParser(parseName(name));
    }


    @Override
    public NameParser getNameParser(String name)
            throws NamingException {
        return dirContext.getNameParser(parseName(name));
    }

    @Override
    public Name composeName(Name name, Name prefix)
            throws NamingException {
        prefix = (Name) name.clone();
        return prefix.addAll(name);
    }

    @Override
    public String composeName(String name, String prefix)
            throws NamingException {
        return prefix + "/" + name;
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal)
            throws NamingException {
        return dirContext.addToEnvironment(propName, propVal);
    }

    @Override
    public Object removeFromEnvironment(String propName)
            throws NamingException {
        return dirContext.removeFromEnvironment(propName);
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




    protected String parseName(String name)
            throws NamingException {
        return name;
    }


    /**
     * Parses a name.
     *
     * @return the parsed name
     */
    protected Name parseName(Name name)
            throws NamingException {
        return name;
    }

    protected CacheEntry cacheLookup(String name) {
        if (cache == null) {

        }

        return null;
    }

    protected boolean cacheLoad(CacheEntry entry) {
        return true;
    }

    protected boolean cacheUnload(String name) {
        if (cache == null)
            return false;
        return (cache.remove(name) != null);
    }


    protected class CacheEntry {


        // ------------------------------------------------- Instance Variables


        long timestamp = -1;
        String name = null;
        ResourceAttributes attributes = null;
        Resource resource = null;
        DirContext context = null;


        // ----------------------------------------------------- Public Methods


        public void recycle() {
            timestamp = -1;
            name = null;
            attributes = null;
            resource = null;
            context = null;
        }


        public String toString() {
            return ("Cache entry: " + name + "\n"
                    + "Attributes: " + attributes + "\n"
                    + "Resource: " + resource + "\n"
                    + "Context: " + context);
        }
    }
}
