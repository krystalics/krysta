package com.krysta.ioc.init;

import java.util.ArrayList;
import java.util.List;

/*
 * This class created on 2019/8/12
 *
 * @author Krysta
 * */
public class DependencyTreeNode {
    private WrapperDefinition wrapperDefinition;

    public boolean circle;

    public List<DependencyTreeNode> next = new ArrayList<>();

    public DependencyTreeNode(WrapperDefinition wrapperDefinition) {
        this.wrapperDefinition = wrapperDefinition;
    }

    public WrapperDefinition getWrapperDefinition() {
        return wrapperDefinition;
    }

    public void replaceNode(DependencyTreeNode node) {
        next = node.next;
    }

    public void clear() {
        clearTree(this);
    }

    public void clearTree(DependencyTreeNode tree) {
        if (tree == null) return;

        for (DependencyTreeNode node : tree.next) {
            clearTree(node);
        }

        tree.next.clear();
    }
}
