package org.apache.naming.resources;


import org.apache.naming.NamingContextEnumeration;
import org.apache.naming.NamingEntry;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import java.io.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * 有两种资源表现形式: 文件 和 目录
 * Created by tisong on 9/15/16.
 */
public class FileDirContext extends BaseDirContext {


    protected static final int BUFFER_SIZE = 2048;


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

        rebind(name, obj, attrs);
    }


    public NamingEnumeration list(String name) throws NamingException {

        File file = findFile(name);

        if (file == null) {

        }

        Vector entires = list(file);

        return new NamingContextEnumeration(entires);
    }

    /**
     * 把File目录下所有子目录和文件抽象为<code>FileDirContext</code>  和 <code>FileResource</code>
     * @param file
     * @return
     */
    protected Vector list(File file) {
        Vector<NamingEntry> entries = new Vector<NamingEntry>();

        String[] names = file.list();

        for (String name: names) {
            File currentFile = new File(file, name);
            Object object = null;
            if (currentFile.isDirectory()) {
                FileDirContext tempContext = new FileDirContext(env);
                tempContext.setDocBase(file.getPath());
                object = tempContext;
            } else {
                object = new FileResource(currentFile);
            }
            entries.addElement(new NamingEntry(name, object, NamingEntry.ENTRY));
        }
        return entries;
    }


    /**
     * 将名称和属性绑定到一个对象中
     * @param name
     * @param obj
     * @param attrs
     * @throws NamingException
     */
    @Override
    public void rebind(String name, Object obj, Attributes attrs) throws NamingException {

        File file = new File(baseFile, name);

        /**
         * 获取 obj 对象的输入流
         */
        InputStream is = null;
        if (obj instanceof Resource) {
            try {
                is = ((Resource) obj).streamContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (obj instanceof InputStream) {
            is = (InputStream) obj;
        } else if (obj instanceof DirContext) {

        }

        /**
         * 将输入流中的内容输出到 name 代表的文件中去
         */
        try {
            FileOutputStream os = null;
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = -1;
            try {
                os = new FileOutputStream(file);
                while (true) {
                    len = is.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    os.write(buffer, 0, len);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    os.close();
                }
                is.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DirContext createSubcontext(String name, Attributes attrs) throws NamingException {
        System.out.println("FileDirContext 创建子组件");

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

        File file = new File(name);

        return new FileResourceAttributes(file);
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
