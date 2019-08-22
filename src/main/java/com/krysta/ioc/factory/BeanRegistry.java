package com.krysta.ioc.factory;

import com.krysta.ioc.AnnotationLoader;
import com.krysta.ioc.BeanDefinition;
import com.krysta.ioc.ScopeType;
import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.DestroyMethod;
import com.krysta.ioc.annotation.InitMethod;
import com.krysta.ioc.annotation.Scope;
import com.krysta.ioc.util.ClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Krysta on 2019/8/21.
 *
 * @description 用于将扫描到的class，注册成BeanDefinition
 * @since ioc1.0
 */
public class BeanRegistry {
    public Map<String, BeanDefinition> definitionMap = new HashMap<>();

    public void registerClasses(Set<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            definitionMap.put(getBeanName(aClass), registerClass(aClass));
        }
    }

    public <T> BeanDefinition registerClass(Class<T> clazz) {
        BeanDefinition definition = new BeanDefinition();
        definition.setClazz(clazz);
        definition.setBeanName(getBeanName(clazz));
        definition.setScopeType(getScopeType(clazz));
        definition.setInitMethod(getInitMethod(clazz));
        definition.setDestroyMethod(getDestroyMethod(clazz));
        definition.setAutowiredFieldsMap(getAllAutowiredFieldsMap(clazz));
        return definition;
    }

    public <T> String getBeanName(Class<T> clazz) {
        return AnnotationLoader.getName(clazz);
    }

    public <T> ScopeType getScopeType(Class<T> clazz) {
        Scope scope = clazz.getAnnotation(Scope.class);
        if (scope == null) {
            return ScopeType.SINGLETON;
        }
        return scope.type();
    }

    public <T> Method getInitMethod(Class<T> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            InitMethod initMethod = method.getAnnotation(InitMethod.class);
            if (initMethod != null) {
                return method;
            }
        }
        return null;
    }

    public <T> Method getDestroyMethod(Class<T> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            DestroyMethod destroyMethod = method.getAnnotation(DestroyMethod.class);
            if (destroyMethod != null) {
                return method;
            }
        }

        return null;
    }

    /*
     * 获得包括父类的field
     * */
    public Map<Field, String> getAllAutowiredFieldsMap(Class<?> clazz) {
        Map<Field, String> map = new HashMap<>();

        List<Class<?>> interfaces = ClassUtil.getAllInterfaces(clazz);
        for (Class<?> anInterface : interfaces) {
            map.putAll(getAutowiredFieldsMap(anInterface));
        }

        return map;
    }

    public <T> Map<Field, String> getAutowiredFieldsMap(Class<T> clazz) {
        Map<Field, String> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null) {
                map.put(field, getBeanName(field.getType()));
            }
        }
        return map;
    }


}
