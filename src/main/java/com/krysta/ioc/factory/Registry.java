package com.krysta.ioc.factory;

import com.krysta.ioc.BeanDefinition;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Krysta on 2019/8/27.
 *
 * @since ioc1.0
 */
public interface Registry {

    void registerClasses(Set<Class<?>> classes);

    List<String> getBeanNamesByType(Class<?> clazz);

    BeanDefinition getBeanDefinition(String beanName);

    Map<String, BeanDefinition> getBeanDefinitionMap();

    List<BeanDefinition> getBeanDefinitions(Class<?> clazz);

}
