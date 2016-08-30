package org.apache.catalina;

import java.util.EventObject;

/**
 * Created by tisong on 8/22/16.
 */
public class ContainerEvent extends EventObject {

    private Object data = null;

    private String type = null;


    /**
     * Constructs a prototypical Event.
     *
     * @param container The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContainerEvent(Container container, String type, Object data) {

        super(container);
        this.type = type;
        this.data = data;

    }

    public Container getContainer() {

        return (Container) getSource();
    }
}
