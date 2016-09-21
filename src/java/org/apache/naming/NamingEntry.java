package org.apache.naming;

/**
 * name - 资源对象
 * Created by tisong on 9/7/16.
 */
public class NamingEntry {

    public static final int ENTRY = 0;
    public static final int LINK_REF = 1;
    public static final int REFERENCE = 2;

    public static final int CONTEXT = 10;


    /**
     * The type instance variable is used to avoid unsing RTTI when doing
     * lookups.
     */
    public int type;
    public String name;
    public Object value;


    public NamingEntry(String name, Object value, int type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

}
