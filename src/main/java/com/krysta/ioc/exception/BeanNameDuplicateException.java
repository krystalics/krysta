package com.krysta.ioc.exception;

/**
 * Created by Krysta on 2019/8/23.
 *
 * @since ioc1.0
 */
public class BeanNameDuplicateException extends RuntimeException{
    private static final long serialVersionUID = -389956377536756L;

    public BeanNameDuplicateException(Class<?> clz1,Class<?> clz2,String beanName) {
        super(clz1.getName()+" "+clz2.getName()+" has the same beanName: "+beanName);
    }

}
