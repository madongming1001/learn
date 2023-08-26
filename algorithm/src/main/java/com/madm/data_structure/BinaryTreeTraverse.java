package com.madm.data_structure;


import org.apache.commons.collections4.CollectionUtils;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

public class BinaryTreeTraverse {
    private Node head;

    public static Node createBinaryTree(LinkedList<Integer> inputList) {
        Node node = null;
        if (CollectionUtils.isEmpty(inputList)) {
            return null;
        }
        Integer data = inputList.removeFirst();
        if (data != null) {
            node = new Node(data);
            node.left = createBinaryTree(inputList);
            node.right = createBinaryTree(inputList);
        }
        return node;
    }

    /**
     * 前序遍历
     *
     * @param node
     */
    public static void preOrderTraveral(Node node) {
        if (Objects.isNull(node)) {
            return;
        }
        System.out.println(node.value);
        preOrderTraveral(node.left);
        preOrderTraveral(node.right);
    }

    /**
     * 中序遍历
     *
     * @param node
     */
    public static void inOrderTraveral(Node node) {
        if (Objects.isNull(node)) {
            return;
        }
        preOrderTraveral(node.left);
        System.out.println(node.value);
        preOrderTraveral(node.right);
    }

    /**
     * 后序遍历
     *
     * @param node
     */
    public static void postOrderTraveral(Node node) {
        if (Objects.isNull(node)) {
            return;
        }
        preOrderTraveral(node.left);
        preOrderTraveral(node.right);
        System.out.println(node.value);
    }

    /**
     * 二叉树非递归前序遍历
     */
    public static void preOrderTraveralWithStack(Node root) {
        Stack<Node> stack = new Stack<Node>();
        Node node = root;
        while (node != null || !stack.isEmpty()) {
            //迭代访问节点的左孩子，并入栈
            while (node != null) {
                System.out.println(node.value);
                stack.push(node);
                node = node.left;
            }
            //如果节点没有左孩子，则弹出栈顶节点，访问节点右孩子
            if (!stack.isEmpty()) {
                node = stack.pop();
                node = node.right;
            }

        }
    }

    /**
     * 二叉树--广度优先遍历--层次遍历法
     *
     * @param root
     */
    public static void breadthFirstSearch(Node root) {
        LinkedList<Node> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.println(node.value);
            if (node.left != null) {
                queue.offer(node.left);
            }
            if (node.right != null) {
                queue.offer(node.right);
            }
        }
    }

    public Node find(int data) {
        Node<Integer> p = head;
        while (p != null) {
            if (data < p.value) p = p.left;
            else if (data > p.value) p = p.right;
            else return p;
        }
        return null;
    }

    public void insert(int data) {
        if (head == null) {
            head = new Node(data);
            return;
        }
        Node<Integer> p = head;
        while (p != null) {
            if (data > p.value) {
                if (p.right == null) {
                    p.right = new Node(data);
                    return;
                }
                p = p.right;
            } else { // data < p.data
                if (p.left == null) {
                    p.left = new Node(data);
                    return;
                }
                p = p.left;
            }
        }
    }

    public void delete(int data) {
        Node<Integer> p = head; // p指向要删除的节点，初始化指向根节点
        Node<Integer> pp = null; // pp记录的是p的父节点
        while (p != null && p.value != data) {
            pp = p;
            if (data > p.value) p = p.right;
            else p = p.left;
        }
        if (p == null) return; // 没有找到

        // 要删除的节点有两个子节点
        if (p.left != null && p.right != null) { // 查找右子树中最小节点
            Node<Integer> minP = p.right;
            Node<Integer> minPP = p; // minPP表示minP的父节点
            while (minP.left != null) {
                minPP = minP;
                minP = minP.left;
            }
            p.value = minP.value; // 将minP的数据替换到p中
            p = minP; // 下面就变成了删除minP了
            pp = minPP;
        }

        // 删除节点是叶子节点或者仅有一个子节点
        Node child; // p的子节点
        if (p.left != null) child = p.left;
        else if (p.right != null) child = p.right;
        else child = null;

        if (pp == null) head = child; // 删除的是根节点
        else if (pp.left == p) pp.left = child;
        else pp.right = child;
    }

    static class Node<T> {
        public Node left;
        public Node right;
        T value;

        public Node(T value) {
            this.value = value;
        }

    }

}
