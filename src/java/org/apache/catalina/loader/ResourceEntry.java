package org.apache.catalina.loader;

import java.net.URL;

/**
 * 应该如何抽象???
 * Created by tisong on 9/18/16.
 */
public class ResourceEntry {

    public long lastModified = -1;


    public Class loadedClass = null;



    public byte[] binaryContent = null;



    public URL source = null;


    public URL codeBase = null;




}
