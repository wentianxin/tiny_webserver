package org.apache.catalina;

import java.beans.PropertyChangeListener;

/**
 * Created by tisong on 8/9/16.
 */
public interface Loader {

    public ClassLoader getClassLoader();


    public Container getContainer();
    public void setContainer(Container container);


    public boolean getDelegate();
    public void setDelegate(boolean delegate);


    public boolean getReloadable();
    public void setReloadable(boolean reloadable);


    public boolean modified();


    public void addRepository(String repository);
    public String[] findRepositories();



    public void addPropertyChangeListener(PropertyChangeListener listener);
    public void removePropertyChangeListener(PropertyChangeListener listener);


}
