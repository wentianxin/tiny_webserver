package org.apache.tomcat.util.net;

/**
 * Created by tisong on 8/21/16.
 */
public abstract class AbstractEndpoint {

    /**
     * Controls when the Endpoint binds the port. <code>true</code>, the default
     * binds the port on {@link #init()} and unbinds it on {@link #destroy()}.
     * If set to <code>false</code> the port is bound on {@link #start()} and
     * unbound on {@link #stop()}.
     */
    private boolean bindOnInit = true;

    public abstract void bind() throws Exception;

    public final void init() throws Exception {
        if (bindOnInit) {
            bind();
            bindState = BindState.BOUND_ON_INIT;
        }
    }

    public static interface Handler {

    }

    protected enum BindState {

    }
}
