package org.apache.catalina;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 流水线组件
 * 最后的阀门 Basic
 *
 * Created by tisong on 8/9/16.
 */
public interface Pipeline {

    // ------------------------------------------------------- Properties

    public Value getBasic();

    public void setBasic(Value value);


    public Value[] getValues();

    public void addValue(Value value);

    public void removeValue(Value value);


    // ------------------------------------------------------- Public Methods

    public void invoke(Request request, Response response) throws IOException, ServletException;
}
