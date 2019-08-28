package com.krysta.ioc.exception;

/**
 * This class created on 2019/8/27
 *
 * @author Krysta
 * @description
 */
public class NoSuchBeanNameException extends RuntimeException {
    private static final long serialVersionUID = -347294204342340234L;

    public NoSuchBeanNameException(String beanName) {
        super("BeanName : "+beanName + " isn't in the container,please check it!");
    }
}

