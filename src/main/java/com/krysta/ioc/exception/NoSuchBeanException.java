package com.krysta.ioc.exception;

/**
 * Created by Krysta on 2019/8/23.
 *
 * @description
 * @since ioc1.0
 */
public class NoSuchBeanException extends RuntimeException {
    private static final long serialVersionUID = -364365364237656L;

    public NoSuchBeanException(Class<?> clazz) {
        super(clazz.getName()+" isn't in the container, please check its annotation.Such as ");
    }
}
