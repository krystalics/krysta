package com.krysta.ioc;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Krysta on 2019/8/21.
 *
 * @since ioc1.0
 */
public class BeanDefinition {
    private Class<?> clazz;
    private String beanName;
    private ScopeType scopeType;
    private Method initMethod;
    private Method destroyMethod;
    private Map<Field, String> autowiredFieldsMap = new HashMap<>();

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public Method getInitMethod() {
        return initMethod;
    }

    public void setInitMethod(Method initMethod) {
        this.initMethod = initMethod;
    }

    public Method getDestroyMethod() {
        return destroyMethod;
    }

    public void setDestroyMethod(Method destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Map<Field, String> getAutowiredFieldsMap() {
        return autowiredFieldsMap;
    }

    public void setAutowiredFieldsMap(Map<Field, String> autowiredFieldsMap) {
        this.autowiredFieldsMap = autowiredFieldsMap;
    }

    public List<Field> getAutowiredFields() {
        List<Field> fields = new ArrayList<>();
        autowiredFieldsMap.forEach((k, v) -> fields.add(k));
        return fields;
    }

    public ScopeType getScopeType() {
        return scopeType;
    }

    public void setScopeType(ScopeType scopeType) {
        this.scopeType = scopeType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += 31 * beanName.hashCode() + 7;
        hash += 31 * clazz.getName().hashCode() + 7;
        hash += 31 * scopeType.hashCode() + 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BeanDefinition definition = (BeanDefinition) obj;
        return clazz.getName().equals(definition.getClazz().getName()) && beanName.equals(definition.getBeanName()) && scopeType.equals(definition.getScopeType());
    }

}
