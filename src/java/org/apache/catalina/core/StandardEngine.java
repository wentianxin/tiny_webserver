package org.apache.catalina.core;

import org.apache.catalina.*;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

/**
 * Created by tisong on 9/3/16.
 */
public class StandardEngine extends ContainerBase
    implements Engine{

    private static final Log logger = LogFactory.getLog(StandardEngine.class);



    private String defaultHost = null;

    private String mapperClass = "org.apache.catalina.core.StandardEngineMapper";

    private Service service = null;


    private DefaultContext defaultContext = null;

    /**
     * 创建对象的同时：绑定流水线上的阀门
     */
    public StandardEngine() {
        super();

        pipeline.setBasic(new StandardEngineValve());

        logger.info("StandardEngine 实例化");
    }


    @Override
    public String getDefaultHost() {
        return this.defaultHost;
    }

    @Override
    public void setDefaultHost(String defaultHost) {
        this.defaultHost = defaultHost;
    }

    @Override
    public void importDefaultContext(Context context) {

    }

    public void setMapperClass(String mapperClass) {

        this.mapperClass = mapperClass;

        // TODO 可触发属性改变的事件
    }

    public String getMapperClass() {
        return mapperClass;
    }


    public void setService(Service service) {
        this.service = service;
    }


    public Service getService() {
        return service;
    }


    /**
     * 当添加子容器的时候, 就会顺带启动了子容器; 但父容器并未启动: 似乎是一个漏洞;
     * @param child
     */
    public void addChild(Container child) {

        if (! (child instanceof Host) ){

            throw new IllegalArgumentException(sm.getString("standardEngine.notHost"));
        }

        super.addChild(child);
    }


    public void start() throws LifecycleException {

        System.out.println("StandardEngine start");

        super.start();
    }


    protected void addDefaultMapper(String mapperClass) {

        // TODO 因为父类是调用子类的属性的, 所以只能重写父类
        super.addDefaultMapper(this.mapperClass);
    }

}
