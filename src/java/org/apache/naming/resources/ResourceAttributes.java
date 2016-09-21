package org.apache.naming.resources;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tisong on 9/18/16.
 */
public class ResourceAttributes implements Attributes {


    public static final String CONTENT_LENGTH = "getcontentlength";

    public static final String CREATION_DATE = "creationdate";

    public static final String ETAG = "getetag";


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


    protected String strongETag = null;
    protected String weakETag = null;


    protected Attributes attributes = null;

    protected static final SimpleDateFormat formats[] = {
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US),
            new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US),
            new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US)
    };


    public ResourceAttributes() {
    }


    public ResourceAttributes(Attributes attributes) {
        this.attributes = attributes;
    }



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


    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public long getLastModified() {
        return lastModefied;
    }


    public Date getCreationDate() {
        if (creationDate != null)
            return creationDate;
        if (creation != -1L) {
            creationDate = new Date(creation);
            return creationDate;
        }
        if (attributes != null) {
            Attribute attribute = attributes.get(CREATION_DATE);
            if (attribute != null) {
                try {
                    Object value = attribute.get();
                    if (value instanceof Long) {
                        creation = ((Long) value).longValue();
                        creationDate = new Date(creation);
                    } else if (value instanceof Date) {
                        creation = ((Date) value).getTime();
                        creationDate = (Date) value;
                    } else {
                        String creationDateValue = value.toString();
                        Date result = null;
                        // Parsing the HTTP Date
                        for (int i = 0; (result == null) &&
                                (i < formats.length); i++) {
                            try {
                                result = formats[i].parse(creationDateValue);
                            } catch (ParseException e) {
                                ;
                            }
                        }
                        if (result != null) {
                            creation = result.getTime();
                            creationDate = result;
                        }
                    }
                } catch (NamingException e) {
                    ; // No value for the attribute
                }
            }
        }
        return creationDate;
    }



    public void setCreationDate(Date creationDate) {
        this.creation = creationDate.getTime();
        this.creationDate = creationDate;
        if (attributes != null)
            attributes.put(CREATION_DATE, creationDate);
    }


    public String getETag() {
        return getETag(false);
    }


    /**
     * Get ETag.
     *
     * @param strong If true, the strong ETag will be returned
     * @return ETag
     */
    public String getETag(boolean strong) {
        String result = null;
        if (attributes != null) {
            Attribute attribute = attributes.get(ETAG);
            if (attribute != null) {
                try {
                    result = attribute.get().toString();
                } catch (NamingException e) {
                    ; // No value for the attribute
                }
            }
        }
        if (strong) {
            // The strong ETag must always be calculated by the resources
            result = strongETag;
        } else {
            // The weakETag is contentLenght + lastModified
            if (weakETag == null) {
                weakETag = "W/\"" + getContentLength() + "-"
                        + getLastModified() + "\"";
            }
            result = weakETag;
        }
        return result;
    }

    @Override
    public boolean isCaseIgnored() {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Attribute get(String attrID) {
        return null;
    }

    @Override
    public NamingEnumeration<? extends Attribute> getAll() {
        return null;
    }

    @Override
    public NamingEnumeration<String> getIDs() {
        return null;
    }

    @Override
    public Attribute put(String attrID, Object val) {
        return null;
    }

    @Override
    public Attribute put(Attribute attr) {
        return null;
    }

    @Override
    public Attribute remove(String attrID) {
        return null;
    }

    public Object clone() {
        return this;
    }
}
