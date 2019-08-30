package com.krysta.ioc.factory;

/**
 * Created by Krysta on 2019/8/27.
 * define the interface to outside of the container
 * @since ioc1.0
 */
public interface Container {
    /**
     * only by class to get bean
     * the class needs to be non-superclass or non-interface
     * @param clazz: the bean's class
     * @return T
     */
    <T> T getBean(Class<T> clazz);

    /**
     * getBean only by its beanName
     * condition is the class does'n have superClass or interface
     * because they will have the same beanName
     *
     * @param beanName: like the bean's id
     * @return Object
     */
    Object getBean(String beanName);

    /**
     * getBean by its beanName and type
     * exactly
     * @param beanName,clazz: used these two params to exactly confirm a inheritance chain's class
     * @return T
     */
    <T> T getBean(String beanName, Class<T> clazz);


}
