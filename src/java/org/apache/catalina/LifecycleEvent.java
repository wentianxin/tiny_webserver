package org.apache.catalina;

import java.util.EventObject;

/**
 * 事件状态对象
 */
public final class LifecycleEvent extends EventObject{


    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {

        super(lifecycle);
        this.type= type;
        this.data = data;
    }

    private Object data = null;

    private String type = null;

    public Object getData() {

        return (this.data);

    }


    /**
     * Return the Lifecycle on which this event occurred.
     */
    public Lifecycle getLifecycle() {

        return (Lifecycle) getSource();

    }


    /**
     * Return the event type of this event.
     */
    public String getType() {

        return (this.type);

    }
}
