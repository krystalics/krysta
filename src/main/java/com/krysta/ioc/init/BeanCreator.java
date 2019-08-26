package com.krysta.ioc.init;


import com.krysta.ioc.AnnotationLoader;
import com.krysta.ioc.BeanDefinition;
import com.krysta.ioc.annotation.Qualifier;
import com.krysta.ioc.exception.InitClassException;
import com.krysta.ioc.factory.BeanRegistry;

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
 * @description 构建依赖树并创建bean，与SwiftBeanFactory对接
 * */

public class BeanCreator {
    private DependencyTreeNode tree; //需要构建的树
    private Map<String, Integer> beanCount = new HashMap<>(); //对autowired重复的BeanDefinition进行编号 ，方便构建出依赖树

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
        DependencyTreeNode root = new DependencyTreeNode(wrapperDefinition); //每次递归都会有一个子节点的树根
        getAllSubNode(root);

        TreeUtils.buildTree(tree, root); //将这一课子树加到总的树中
        TreeUtils.isCircle(tree); //如果有循环依赖，抛出异常

        for (int i = 0; i < root.next.size(); i++) {
            recursion(root.next.get(i).getWrapperDefinition());
        }
    }

    /*
     * 获得root的所有子节点，子节点会将自己的继承链都加入进root，用于判断继承链中的循环依赖
     * */
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
        List<Class<?>> classes = new ArrayList<>();
        while (clazz != null) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
        List<BeanDefinition> beanDefinitionList = new ArrayList<>();
        for (Class<?> aClass : classes) {
            List<String> beanNames = BeanRegistry.INSTANCE.getBeanNamesByType(aClass);
            if (AnnotationLoader.inContainer(aClass)) {
                for (String beanName : beanNames) {
                    BeanDefinition beanDefinition = BeanRegistry.INSTANCE.getBeanDefinition(beanName);
                    if (beanDefinition.getClazz().equals(aClass)) {
                        beanDefinitionList.add(beanDefinition);
                        break;
                    }
                }
            } else if (beanNames != null && aClass.equals(field.getType())) { //只有当前类没有在容器中时，会走下面的逻辑
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

        return beanDefinitionList;
    }


    /*
     * 后序遍历该树，构造整个依赖树
     * */
    public void createBeansByTree(DependencyTreeNode root) {
        if (root == null) {
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
            Object singletonObject = createBean(definition.getClazz(), definition.getAutowiredFieldsMap());
            singletonObjects.put(definition.getBeanName(), singletonObject);
            beanNamesLoaded.add(definition.getBeanName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }


    public <T> T createBean(Class<T> tClass, Map<Field, String> fields) throws Exception {

        checkModifier(tClass); //如果注解了SwiftBean的类不是public的，抛出异常
        checkConstructor(tClass); //检查是否包含一个空的构造函数

        final Constructor<T> declaredConstructor = tClass.getDeclaredConstructor();
        declaredConstructor.setAccessible(true);
        T bean = declaredConstructor.newInstance();


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
