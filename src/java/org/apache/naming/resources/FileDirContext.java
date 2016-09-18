package org.apache.naming.resources;


import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Created by tisong on 9/15/16.
 */
public class FileDirContext extends BaseDirContext {


    protected File baseFile = null;


    protected String absoluteBase = null;



    public FileDirContext() {
        super();
    }

    public FileDirContext(Hashtable env) {
        super(env);
    }


    /**
     * 寻找资源: 两种表现形式 目录(FileDirContent)与文件(FileResource)
     * @param name
     * @return
     * @throws NamingException
     */
    @Override
    public Object lookup(String name) throws NamingException {

        Object result = null;

        File file = findFile(name);

        if (file.isDirectory()) {
            FileDirContext tempContext = new FileDirContext(env);
            tempContext.setDocBase(file.getPath());
            result = tempContext;
        } else {
            result = new FileResource(file);
        }

        return result;
    }


    @Override
    public void bind(String name, Object obj, Attributes attrs) throws NamingException {

    }


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


    protected File findFile(String name) {

        File file = new File(baseFile, name);


        return file;
    }



    public void setDocBase(String docBase) {

        baseFile = new File(docBase);

        try {
            baseFile = baseFile.getCanonicalFile();
        } catch (IOException e) {
            // Ignore
        }

        absoluteBase = baseFile.getAbsolutePath();

        super.setDocBase(docBase);
    }



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





    protected class FileResource extends Resource {


        protected File file = null;

        protected long length = -1L;

        public FileResource(File file) {
            this.file = file;
        }


        public InputStream streamContent() throws IOException {

            if (binaryContent != null) {
                inputStream = new FileInputStream(file);
            }

            return super.streamContent();
        }
    }


    protected class FileResourceAttributes extends ResourceAttributes {

        protected File file = null;


        public FileResourceAttributes(File file) {
            this.file = file;
        }

        public long getContentLength() {
            if (contentLength != -1L)
                return contentLength;
            contentLength = file.length();
            return contentLength;
        }

    }
}
