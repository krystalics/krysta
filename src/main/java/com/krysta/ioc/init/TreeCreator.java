package com.krysta.ioc.init;


import com.krysta.ioc.BeanDefinition;
import com.krysta.ioc.util.KrystaLogger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * This class created on 2019/8/12
 *
 * @author Krysta
 * */
public class TreeCreator {

    private static List<BeanDefinition> path = new ArrayList<>();

    private boolean circle;

    /*
     * build the tree from DFS algorithm to replace the tree's leaf with new root node
     * */
    public void buildTree(DependencyTreeNode tree, DependencyTreeNode root) {
        if (tree == null || root.circle) return;

        for (DependencyTreeNode node : tree.next) {
            buildTree(node, root);
        }

        if (tree.getWrapperDefinition().equals(root.getWrapperDefinition())) {
            tree.replaceNode(root);
        }

    }

    public boolean judgeDependency(DependencyTreeNode root){
        isCircle(root);
        return circle;
    }

    /*
     * judge circle dependency by a chain has duplicated node
     * */
    public void isCircle(DependencyTreeNode root) {
        if (root == null || root.circle) return;
        path.add(root.getWrapperDefinition().definition);

        for (DependencyTreeNode node : root.next) {
            isCircle(node);
        }
        if (!path.isEmpty()) {
            Set<BeanDefinition> set = new HashSet<>();
            BeanDefinition definition = null;

            for (BeanDefinition beanDefinition : path) {
                if (set.contains(beanDefinition)) {
                    definition = beanDefinition;
                    break;
                } else {
                    set.add(beanDefinition);
                }
            }

            if (definition != null) { //it has duplicate node to dead circle
                root.circle = true;
                circle=true;
                circleError(definition);
            }

            path.remove(path.size() - 1); //delete the leaf ,to judge another chain
        }
    }


    private static void circleError(BeanDefinition definition) {
        int index = -1;
        for (int i = 0; i < path.size(); i++) {
            if (definition.equals(path.get(i))) {
                index = i;
                break;
            }
        }

        StringBuilder errorMsg = new StringBuilder();
        for (int i = index; i < path.size(); i++) {
            errorMsg.append("\n").append(path.get(i).getClazz().getName());
        }

        KrystaLogger.INSTANCE.debug(errorMsg.toString() + " has a dead circle dependency");

    }
}
