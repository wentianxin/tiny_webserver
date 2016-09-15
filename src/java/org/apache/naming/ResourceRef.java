package org.apache.naming;

import javax.naming.Reference;
import javax.naming.StringRefAddr;

/**
 * 描述一个引用的引用地址
 * Created by tisong on 9/8/16.
 */
public class ResourceRef extends Reference{

    /**
     * org.apache.naming.factory.ResourceFactory
     */
    public static final String DEFAULT_FACTORY =
            org.apache.naming.factory.Constants.DEFAULT_RESOURCE_FACTORY;


    /**
     * Description address type.
     */
    public static final String DESCRIPTION = "description";


    /**
     * Scope address type.
     */
    public static final String SCOPE = "scope";


    /**
     * Auth address type.
     */
    public static final String AUTH = "auth";



    public ResourceRef(String resourceClass, String description,
                       String scope, String auth) {
        this(resourceClass, description, scope, auth, null, null);
    }

    public ResourceRef(String resourceClass, String description,
                       String scope, String auth, String factory,
                       String factoryLocation) {
        /**
         * Constructs a new reference for an object with class name 'className',
         * and the class name and location of the object's factory.
         */
        super(resourceClass, factory, factoryLocation);
        StringRefAddr refAddr = null;
        if (description != null) {
            refAddr = new StringRefAddr(DESCRIPTION, description);
            add(refAddr);
        }
        if (scope != null) {
            refAddr = new StringRefAddr(SCOPE, scope);
            add(refAddr);
        }
        if (auth != null) {
            refAddr = new StringRefAddr(AUTH, auth);
            add(refAddr);
        }
    }
}
