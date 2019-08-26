package com.krysta.ioc.factory;

import com.krysta.ioc.AnnotationLoader;
import com.krysta.ioc.BeanDefinition;
import com.krysta.ioc.ScopeType;
import com.krysta.ioc.annotation.Autowired;
import com.krysta.ioc.annotation.DestroyMethod;
import com.krysta.ioc.annotation.InitMethod;
import com.krysta.ioc.annotation.Scope;
import com.krysta.ioc.exception.BeanNameDuplicateException;
import com.krysta.ioc.exception.NoSuchBeanException;
import com.krysta.ioc.util.ClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Krysta on 2019/8/21.
 *
 * @description 用于将扫描到的class，注册成BeanDefinition
 * @since ioc1.0
 */
public abstract class AbstractBeanRegistry {
    public Map<String, BeanDefinition> definitionMap = new HashMap<>();
    public Set<String> beanNamesNotLoaded = new HashSet<>();

    public Map<Class<?>, List<BeanDefinition>> classBeanDefinitionListMap = new HashMap<>();
    public Map<Class<?>, List<String>> typeBeanNames = new HashMap<>();

    public void registerClasses(Set<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            BeanDefinition definition = new BeanDefinition();

            String beanName = getBeanName(aClass);
            checkDuplicateBeanName(beanName, aClass);

            setSuperBeanName(aClass, beanName);
            //初步注册
            definition.setClazz(aClass);
            definition.setBeanName(beanName);
            definitionMap.put(beanName, definition);
        }

        for (Map.Entry<String, BeanDefinition> definitionEntry : definitionMap.entrySet()) {
            completeRegister(definitionEntry.getValue());
        }

        setClassBeanDefinitionListMap();
    }

    private void setClassBeanDefinitionListMap() {
        typeBeanNames.forEach((k, v) -> {
            List<BeanDefinition> list = new ArrayList<>();
            for (String s : v) {
                list.add(definitionMap.get(s));
            }
            classBeanDefinitionListMap.put(k, list);
        });
    }

    /**
     * 将该BeanName也注册到父类和接口中，方便后续使用容器时，面向接口编程。
     */
    private void setSuperBeanName(Class<?> clazz, String beanName) {
        Set<Class<?>> interfaces = ClassUtil.getAllInterfacesAndSelf(clazz);
        for (Class<?> anInterface : interfaces) {
            if (typeBeanNames.get(anInterface) == null) {
                List<String> list = new ArrayList<>();
                list.add(beanName);
                typeBeanNames.put(anInterface, list);
            } else {
                typeBeanNames.get(anInterface).add(beanName);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void completeRegister(BeanDefinition definition) {
        Class clazz = definition.getClazz();
        definition.setScopeType(getScopeType(clazz));
        definition.setInitMethod(getInitMethod(clazz));
        definition.setDestroyMethod(getDestroyMethod(clazz));
        definition.setAutowiredFieldsMap(getAllAutowiredFieldsMap(clazz));
    }

    private <T> String getBeanName(Class<T> clazz) {
        return AnnotationLoader.getName(clazz);
    }

    private <T> void checkDuplicateBeanName(String beanName, Class<T> clazz) {
        if (!beanNamesNotLoaded.contains(beanName)) {
            beanNamesNotLoaded.add(beanName);
        } else {
            definitionMap.forEach((k, v) -> {
                if (v.getBeanName().equals(beanName)) {
                    throw new BeanNameDuplicateException(v.getClazz(), clazz, beanName);
                }
            });
        }
    }

    private <T> ScopeType getScopeType(Class<T> clazz) {
        Scope scope = clazz.getAnnotation(Scope.class);
        if (scope == null) {
            return ScopeType.SINGLETON;
        }
        return scope.type();
    }

    private <T> Method getInitMethod(Class<T> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            InitMethod initMethod = method.getAnnotation(InitMethod.class);
            if (initMethod != null) {
                return method;
            }
        }
        return null;
    }

    private <T> Method getDestroyMethod(Class<T> clazz) {
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
    private Map<Field, String> getAllAutowiredFieldsMap(Class<?> clazz) {
        Map<Field, String> map = new HashMap<>();

        Set<Class<?>> interfaces = ClassUtil.getAllInterfacesAndSelf(clazz);
        for (Class<?> anInterface : interfaces) {
            map.putAll(getAutowiredFieldsMap(anInterface));
        }

        return map;
    }

    private <T> Map<Field, String> getAutowiredFieldsMap(Class<T> clazz) {
        Map<Field, String> map = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired != null) {
                checkNoSuchBean(field.getType());
                map.put(field, getBeanName(field.getType()));
            }
        }
        return map;
    }

    private <T> void checkNoSuchBean(Class<T> clazz) {
        boolean contains = false;
        for (Map.Entry<String, BeanDefinition> definitionEntry : definitionMap.entrySet()) {
            if (definitionEntry.getValue().getClazz().equals(clazz)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            throw new NoSuchBeanException(clazz);
        }
    }

}
