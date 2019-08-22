package com.krysta.ioc.util;

import java.util.ArrayList;
import java.util.List;

public class ClassUtil {
    public static List<Class<?>> getAllInterfaces(Class<?> clazz){
        List<Class<?>> interfaces=new ArrayList<>();
        while(clazz!=null){
            interfaces.add(clazz);
            clazz= clazz.getSuperclass();
        }
        return interfaces;
    }
}
