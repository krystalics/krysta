package com.krysta.ioc.init;

import java.util.ArrayList;
import java.util.List;

/*
 * This class created on 2019/8/12
 *
 * @author Krysta
 * */
class DependencyTreeNode {
    private WrapperDefinition wrapperDefinition;

    boolean circle;

    List<DependencyTreeNode> next = new ArrayList<>();

    DependencyTreeNode(WrapperDefinition wrapperDefinition) {
        this.wrapperDefinition = wrapperDefinition;
    }

    WrapperDefinition getWrapperDefinition() {
        return wrapperDefinition;
    }

    void replaceNode(DependencyTreeNode node) {
        next = node.next;
    }

    void clear() {
        clearTree(this);
    }

    private void clearTree(DependencyTreeNode tree) {
        if (tree == null) return;

        for (DependencyTreeNode node : tree.next) {
            clearTree(node);
        }

        tree.next.clear();
    }
}
