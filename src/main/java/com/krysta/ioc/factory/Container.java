package com.krysta.ioc.factory;

/**
 * Created by Krysta on 2019/8/27.
 *
 * @description 定义容器堆外开放的接口
 * @since ioc1.0
 */
public interface Container {
    /**
     * 仅通过class信息来获得容器中对应的bean
     * 需要该类没有多个BeanName，即没有子类也在容器中
     */
    <T> T getBean(Class<T> clazz);

    /**
     * getBean only by its beanName
     * condition is the class does'n have superClass or interface
     * because they will have the same beanName
     */
    Object getBean(String beanName);

    /**
     * getBean by its beanName and type
     * exactly
     */
    <T> T getBean(String beanName, Class<T> clazz);

    /**
     * getBean by its beanName , type and parameters
     * used in prototype,not singleton
     */
    <T> T getBean(String beanName, Class<T> clazz, Object... params);


}
