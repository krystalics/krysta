package com.krysta.ioc.exception;

/**
 * This class created on 2019/8/20
 *
 * @author Krysta
 */
public class InitClassException extends RuntimeException {
    private static final long serialVersionUID = -389954783924756L;

    public InitClassException(String message) {
        super(message);
    }

}
