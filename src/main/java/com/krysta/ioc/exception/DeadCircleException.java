package com.krysta.ioc.exception;

/**
 * Created by Krysta on 2019/8/20.
 *
 * @description
 * @since ioc1.0
 */
public class DeadCircleException extends RuntimeException {
    public static final long serialVersionUID=-23231782232449198L;

    public DeadCircleException(String message){
        super(message);
    }
}
