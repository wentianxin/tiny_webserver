package org.apache.naming.resources;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 对资源的封装: 两种形式 byte[] or inputstream
 */
public class Resource {

    protected byte[] binaryContent = null;


    protected InputStream inputStream = null;

    public Resource() {
    }

    public Resource(InputStream inputStream) {
        setContent(inputStream);
    }


    public Resource(byte[] binaryContent) {
        setContent(binaryContent);
    }



    public InputStream streamContent() throws IOException {
        if (binaryContent != null) {
            return new ByteArrayInputStream(binaryContent);
        }

        return inputStream;
    }


    public byte[] getContent() {
        return binaryContent;
    }

    public void setContent(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setContent(byte[] binaryContent) {
        this.binaryContent = binaryContent;
    }
}
