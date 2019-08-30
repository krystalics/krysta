package com.krysta.ioc.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Krysta on 2019/8/26.
 *
 * @since ioc1.0
 */
public class ClassUtil {

    public static Set<Class<?>> getAllInterfacesAndSelf(Class<?> clazz) {
        Set<Class<?>> classes = new HashSet<>();
        if(!clazz.isInterface()&&!clazz.equals(Object.class)){
            classes.add(clazz);
        }
        classes.addAll(getAllInterfaces(clazz));
        return classes;
    }

    private static Set<Class<?>> getAllInterfaces(Class<?> clazz){
        Set<Class<?>> classes=new HashSet<>();
        if(clazz.isInterface()){
            classes.add(clazz);
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> anInterface : interfaces) {
            classes.addAll(getAllInterfaces(anInterface));
        }
        if(clazz.getSuperclass()!=null){
            classes.addAll(getAllInterfacesAndSelf(clazz.getSuperclass()));
        }
        return classes;
    }

}
