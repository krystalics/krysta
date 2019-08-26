package com.krysta.ioc.factory;

import com.krysta.ioc.BeanDefinition;

import java.util.List;
import java.util.Map;

public class BeanRegistry extends AbstractBeanRegistry{
    public static final BeanRegistry INSTANCE=new BeanRegistry();

    public List<String> getBeanNamesByType(Class<?> clazz){
        return typeBeanNames.get(clazz);
    }

    public BeanDefinition getBeanDefinition(String beanName){
        return definitionMap.get(beanName);
    }

    public Map<String,BeanDefinition> getBeanDefinitionMap(){
        return definitionMap;
    }

}
