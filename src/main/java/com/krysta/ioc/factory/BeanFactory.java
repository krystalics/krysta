package com.krysta.ioc.factory;

import com.krysta.ioc.BeanDefinition;
import com.krysta.ioc.ClassScanner;
import com.krysta.ioc.ScopeType;
import com.krysta.ioc.exception.GetBeanException;
import com.krysta.ioc.exception.NoSuchBeanException;
import com.krysta.ioc.exception.NoSuchBeanNameException;
import com.krysta.ioc.init.BeanCreator;
import com.krysta.ioc.util.KrystaLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Krysta on 2019/8/22.
 *
 * @description
 * @since ioc1.0
 */
public class BeanFactory implements Container {

    private List<String> packages = new ArrayList<>();

    private List<String> beanNamesLoaded = new ArrayList<>();
    private Map<String, Object> singletonObjects = new HashMap<>();

    private BeanRegistry beanRegistry = BeanRegistry.INSTANCE;
    private List<String> beanNamesNotLoaded = new ArrayList<>();

    public void init() {
        String[] pkgs = new String[packages.size()];
        for (int i = 0; i < packages.size(); i++) {
            pkgs[i] = packages.get(i);
        }
        Set<Class<?>> classes = ClassScanner.scan(pkgs); //扫描这些包
        beanRegistry.registerClasses(classes); //注册

        beanRegistry.getBeanDefinitionMap().forEach((k, v) -> {
            if (v.getScopeType().equals(ScopeType.SINGLETON)) { //将所有单例的加进容器
                beanNamesNotLoaded.add(k);
            }
        });

        while (!beanNamesNotLoaded.isEmpty()) {
            recursionCreateBean(beanRegistry.getBeanDefinitionMap()); //将所有加载过的beanName都删掉
        }

        KrystaLogger.INSTANCE.info("KrystaContainer has been initialized successfully");

    }

    private void recursionCreateBean(Map<String, BeanDefinition> beanDefinitionMap) {
        BeanCreator beanCreator = new BeanCreator(beanNamesLoaded, singletonObjects);
        for (String beanName : beanNamesNotLoaded) {
            beanCreator.buildTreeByBeanName(beanName, beanDefinitionMap);
        }
        beanNamesNotLoaded.removeAll(beanNamesLoaded);

    }

    public List<String> getPackages() {
        return packages;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }


    @Override
    public <T> T getBean(Class<T> clazz) {
        List<String> beanNames = beanRegistry.getBeanNamesByType(clazz);
        if (beanNames == null) {
            throw new NoSuchBeanException(clazz);
        }
        if (beanNames.size() > 1) {
            throw new GetBeanException(clazz, " has more than 2 beanNames,please select one of them");
        }
        return getBean(beanNames.get(0), clazz);
    }

    @Override
    public Object getBean(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        if (isMatched(beanName, clazz)) {
            return (T) singletonObjects.get(beanName);
        } else {
            throw new GetBeanException(clazz, " can not match the beanname");
        }
    }

    private boolean isMatched(String beanName, Class<?> clazz) {
        checkBeanName(beanName);
        boolean flag=false;
        List<String> beanNames = beanRegistry.getBeanNamesByType(clazz);
        for (String name : beanNames) {
            if(name.equals(beanName)){
                flag=true;
                break;
            }
        }
        return flag;
    }

    private void checkBeanName(String beanName) {
        if (beanRegistry.getBeanDefinition(beanName) == null) {
            throw new NoSuchBeanNameException(beanName);
        }
    }


}
