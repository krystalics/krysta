package com.krysta.ioc;

import com.krysta.ioc.factory.BeanFactory;


/**
 * This class created on 2019/8/27
 *
 * @author Krysta
 * @description
 */
public class ApplicationContext extends BeanFactory {
    private volatile static ApplicationContext INSTANCE;

    private ApplicationContext() {
    }

    public static ApplicationContext getContext() {
        if(INSTANCE==null){
            synchronized (ApplicationContext.class) {
                if(INSTANCE==null){
                    INSTANCE = new ApplicationContext();
                }
            }
        }

        return INSTANCE;
    }

}
