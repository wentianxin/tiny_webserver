package org.apache.naming.resources;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.util.Date;

/**
 * Created by tisong on 9/18/16.
 */
public class ResourceAttributes implements Attribute {


    public static final String CONTENT_LENGTH = "getcontentlength";




    protected long contentLength = -1L;

    /**
     * 创建时间
     */
    protected Date creationDate = null;
    protected long creation = -1L;

    /**
     * 最后修改时间
     */
    protected Date lastModifiedDate = null;
    protected long lastModefied = -1L;


    protected Attributes attributes = null;


    public long getContentLength() {

        if (contentLength != -1) {
            return contentLength;
        }

        if (attributes != null) {
            Attribute attribute = attributes.get(CONTENT_LENGTH);
            if (attribute != null) {
                try {
                    Object value = attribute.get();
                    if (value instanceof Long) {
                        contentLength = ((Long) value).longValue();
                    } else {
                        try {
                            contentLength = Long.parseLong(value.toString());
                        } catch (NumberFormatException e) {
                            ; // Ignore
                        }
                    }
                } catch (NamingException e) {
                    ; // No value for the attribute
                }
            }
        }
        return contentLength;
    }


    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
        if (attributes != null) {
            attributes.put(CONTENT_LENGTH, new Long(contentLength));
        }
    }


    @Override
    public NamingEnumeration<?> getAll() throws NamingException {
        return null;
    }

    @Override
    public Object get() throws NamingException {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public boolean contains(Object attrVal) {
        return false;
    }

    @Override
    public boolean add(Object attrVal) {
        return false;
    }

    @Override
    public boolean remove(Object attrval) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public DirContext getAttributeSyntaxDefinition() throws NamingException {
        return null;
    }

    @Override
    public DirContext getAttributeDefinition() throws NamingException {
        return null;
    }

    @Override
    public boolean isOrdered() {
        return false;
    }

    @Override
    public Object get(int ix) throws NamingException {
        return null;
    }

    @Override
    public Object remove(int ix) {
        return null;
    }

    @Override
    public void add(int ix, Object attrVal) {

    }

    @Override
    public Object set(int ix, Object attrVal) {
        return null;
    }

    @Override
    public Object clone() {
        return this;
    }
}
