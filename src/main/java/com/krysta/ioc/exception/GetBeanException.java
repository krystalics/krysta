package com.krysta.ioc.exception;


/**
 * This class created on 2019/8/27
 *
 * @author Krysta
 * @description
 */
public class GetBeanException extends RuntimeException {
    private static final long serialVersionUID = -237219879834923432L;

    public GetBeanException(Class<?> clazz, String msg) {
        super(clazz.getName() + msg);
    }
}
