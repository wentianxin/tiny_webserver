package org.apache.catalina;

import java.util.EventObject;

/**
 * Created by tisong on 9/5/16.
 */
public final class ContainerEvent extends EventObject{

    private Object data = null; // 数据

    private String type = null;

    private Container container = null;


    public ContainerEvent(Container container, String type, Object data) {

        super(container);
        this.container = container;
        this.type =type;
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public String getType() {
        return type;
    }

    public Container getContainer() {
        return container;
    }


    public String toString() {
        return ("ContainerEvent['" + getContainer().getName() + "','" +
                getType() + "']");

    }
}
