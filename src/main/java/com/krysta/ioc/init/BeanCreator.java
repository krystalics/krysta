package com.krysta.ioc.init;


import com.krysta.ioc.factory.BeanRegistry;
import com.krysta.ioc.util.AnnotationUtil;
import com.krysta.ioc.BeanDefinition;
import com.krysta.ioc.annotation.Qualifier;
import com.krysta.ioc.exception.InitClassException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * This class created on 2019/8/13
 *
 * @author Krysta
 * build the dependency tree and create bean from bottom
 * */

public class BeanCreator {
    private DependencyTreeNode tree;
    private Map<String, Integer> beanCount = new HashMap<>();

    private final List<String> beanNamesLoaded;
    private final Map<String, Object> singletonObjects;

    public BeanCreator(List<String> beanNamesLoaded, Map<String, Object> singletonObjects) {
        this.beanNamesLoaded = beanNamesLoaded;
        this.singletonObjects = singletonObjects;
    }

    public void buildTreeByBeanName(String beanName, Map<String, BeanDefinition> beanDefinitionMap) {
        BeanDefinition definition = beanDefinitionMap.get(beanName);

        if (beanNamesLoaded.contains(definition.getBeanName())) {
            return;
        }
        Class<?> clazz = definition.getClazz();
        List<BeanDefinition> definitions = BeanRegistry.INSTANCE.getBeanDefinitions(clazz);
        for (BeanDefinition beanDefinition : definitions) {
            WrapperDefinition wrapperDefinition = new WrapperDefinition(beanDefinition, 0);
            tree = new DependencyTreeNode(wrapperDefinition);
            recursion(wrapperDefinition);
            createBeansByTree(tree);
            tree.clear();
        }
    }

    private void recursion(WrapperDefinition wrapperDefinition) {
        if (wrapperDefinition.getDefinition().getAutowiredFields().isEmpty()) {
            return;
        }
        DependencyTreeNode root = new DependencyTreeNode(wrapperDefinition);
        getAllSubNode(root);

        TreeCreator treeCreator=new TreeCreator();
        treeCreator.buildTree(tree, root);
        if (treeCreator.judgeDependency(tree)) {
            return; //if has a dead circle then jump out
        }

        for (int i = 0; i < root.next.size(); i++) {
            recursion(root.next.get(i).getWrapperDefinition());
        }
    }

    private void getAllSubNode(DependencyTreeNode root) {
        WrapperDefinition wrapperDefinition = root.getWrapperDefinition();
        List<Field> autowireds = wrapperDefinition.getDefinition().getAutowiredFields();
        for (Field autowired : autowireds) {
            List<BeanDefinition> definitions = getBeanDefinitionsByInheritanceChain(autowired.getType(), autowired);
            if (definitions.size() > 0) {
                for (BeanDefinition definition : definitions) {
                    if (beanCount.containsKey(autowired.getName())) {
                        beanCount.put(autowired.getName(), beanCount.get(autowired.getName()) + 1);
                    } else {
                        beanCount.put(autowired.getName(), 1);
                    }
                    DependencyTreeNode node = new DependencyTreeNode(new WrapperDefinition(definition, beanCount.get(autowired.getName())));
                    root.next.add(node);
                }
            }
        }

    }

    private List<BeanDefinition> getBeanDefinitionsByInheritanceChain(Class<?> clazz, Field field) {
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        if (!AnnotationUtil.inContainer(clazz)) {
            List<String> beanNames = BeanRegistry.INSTANCE.getBeanNamesByType(clazz);
            if (beanNames != null) {
                if (beanNames.size() > 1) {
                    Qualifier qualifier = field.getAnnotation(Qualifier.class);
                    if (qualifier != null) {
                        beanDefinitionList.addAll(getBeanDefinitionsByInheritanceChain(BeanRegistry.INSTANCE.getBeanDefinition(qualifier.name()).getClazz(), null));
                    }
                } else if (beanNames.size() == 1) {
                    beanDefinitionList.addAll(getBeanDefinitionsByInheritanceChain(BeanRegistry.INSTANCE.getBeanDefinition(beanNames.get(0)).getClazz(), null));
                }
            }
        }

        List<Class<?>> classes = new ArrayList<>();
        while (clazz != null && !clazz.equals(Object.class)) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
        for (Class<?> aClass : classes) {
            List<String> beanNames = BeanRegistry.INSTANCE.getBeanNamesByType(aClass);

            for (String beanName : beanNames) {
                BeanDefinition beanDefinition = BeanRegistry.INSTANCE.getBeanDefinition(beanName);
                if (beanDefinition.getClazz().equals(aClass)) {
                    beanDefinitionList.add(beanDefinition);
                    break;
                }
            }
        }

        return beanDefinitionList;
    }

    private void createBeansByTree(DependencyTreeNode root) {
        if (root == null || root.circle) {
            return;
        }

        for (DependencyTreeNode node : root.next) {
            createBeansByTree(node);
        }

        putBeanToContainer(root);

    }

    private void putBeanToContainer(DependencyTreeNode root) {
        try {
            BeanDefinition definition = root.getWrapperDefinition().getDefinition();
            if (beanNamesLoaded.contains(definition.getBeanName())) {
                return;
            }
            createBean(definition);
            beanNamesLoaded.add(definition.getBeanName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }


    private  <T> T createBean(BeanDefinition beanDefinition) throws Exception {

        Class<T> clazz = (Class<T>) beanDefinition.getClazz();
        Map<Field, String> fields = beanDefinition.getAutowiredFieldsMap();

        checkModifier(clazz);
        checkConstructor(clazz);

        final Constructor<T> declaredConstructor = clazz.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        T bean = (T) singletonObjects.get(beanDefinition.getBeanName());


        for (Field field : fields.keySet()) {
            field.setAccessible(true);
            String beanName = fields.get(field);
            field.set(bean, singletonObjects.get(beanName));
        }

        return bean;
    }

    private void checkModifier(Class<?> tClass) {
        if (!Modifier.isPublic(tClass.getModifiers())) {
            throw new InitClassException(tClass.getName() + " is not public, can't modifier by reflection");
        }
    }

    private void checkConstructor(Class<?> tClass) {
        Constructor[] constructors = tClass.getDeclaredConstructors();
        boolean flag = false;
        for (Constructor constructor : constructors) {
            Class[] classes = constructor.getParameterTypes();
            if (classes.length == 0) {
                constructor.setAccessible(true);
                flag = true;
                break;
            }
        }

        if (!flag) {
            throw new InitClassException(tClass.getName() + " does not have a empty constructor，can't create bean for this class");
        }

    }
}
