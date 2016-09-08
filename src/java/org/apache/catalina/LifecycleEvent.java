package org.apache.catalina;

import java.util.EventObject;

/**
 * Created by tisong on 9/5/16.
 */
public final class LifecycleEvent extends EventObject{


    private String type = null;

    private Object data = null;


    /**
     * Constructs a prototypical Event.
     *
     * @param lifecycle The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public LifecycleEvent(Lifecycle lifecycle, String type) {
        this(lifecycle, type, null);
    }

    public LifecycleEvent(Lifecycle lifecycle, String type, Object data) {

        super(lifecycle);
        this.type = type;
        this.data = data;
    }


    public Object getData() {
        return data;
    }

    public String getType() {
        return type;
    }


    public Lifecycle getLifecycle() {
        return (Lifecycle) getSource();
    }
}
