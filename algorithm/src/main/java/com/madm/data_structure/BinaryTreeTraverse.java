package com.madm.data_structure;


import cn.hutool.core.collection.CollUtil;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

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
     * 1、弹出就打印
     * 2、如果有右孩子，就压入右孩子
     * 3、如果有左孩子，就压入左孩子
     */
    public static void pre(Node head) {
        System.out.println("pre-order: ");
        if (head != null) {
            Stack<Node> stack = new Stack<>();
            stack.add(head);
            while (CollUtil.isNotEmpty(stack)) {
                head = stack.pop();
                System.out.println(head.value + " ");
                if (head.right != null) {
                    stack.push(head.right);
                }
                if (head.left != null) {
                    stack.push(head.left);
                }
            }
        }
    }

    /**
     * 1、整条左边界依次入栈
     * 2、1无法继续，弹出节点就打印，来到右树节点
     * 中序遍历
     */
    public static void in(Node cur) {
        System.out.print("in-order: ");
        if (cur != null) {
            Stack<Node> stack = new Stack<Node>();
            while (!stack.isEmpty() || cur != null) {
                if (cur != null) {
                    stack.push(cur);
                    cur = cur.left;
                } else {
                    cur = stack.pop();
                    System.out.print(cur.value + " ");
                    cur = cur.right;
                }
            }
        }
        System.out.println();
    }


    /**
     * 1、弹出就放入到一个栈里面
     * 2、如果有左孩子，就压入左孩子
     * 3、如果有右孩子，就压入右孩子
     * 4、最后输出栈
     */
    public static void pos1(Node head) {
        System.out.print("pos-order: ");
        if (head != null) {
            Stack<Node> s1 = new Stack<>();
            Stack<Node> s2 = new Stack<>();
            s1.push(head);
            while (!s1.isEmpty()) {
                head = s1.pop(); // 头 右 左
                s2.push(head);
                if (head.left != null) {
                    s1.push(head.left);
                }
                if (head.right != null) {
                    s1.push(head.right);
                }
            }
            // 左 右 头
            while (!s2.isEmpty()) {
                System.out.print(s2.pop().value + " ");
            }
        }
        System.out.println();
    }

    /**
     * 利用一个栈实现后序遍历
     */
    public static void pos2(Node h) {
        System.out.print("pos-order: ");
        if (h != null) {
            Stack<Node> stack = new Stack<Node>();
            stack.push(h);
            Node c = null;
            while (!stack.isEmpty()) {
                c = stack.peek();
                if (c.left != null && h != c.left && h != c.right) {
                    stack.push(c.left);
                } else if (c.right != null && h != c.right) {
                    stack.push(c.right);
                } else {
                    System.out.print(stack.pop().value + " ");
                    h = c;
                }
            }
        }
        System.out.println();
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
     * 找到层数节点最多的返回树
     */
    public static int maxWidthUseMap(Node head) {
        if (head == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        // key 在 哪一层，value
        HashMap<Node, Integer> levelMap = new HashMap<>();
        levelMap.put(head, 1);
        int curLevel = 1; // 当前你正在统计哪一层的宽度
        int curLevelNodes = 0; // 当前层curLevel层，宽度目前是多少
        int max = 0;
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            int curNodeLevel = levelMap.get(cur);
            if (cur.left != null) {
                levelMap.put(cur.left, curNodeLevel + 1);
                queue.add(cur.left);
            }
            if (cur.right != null) {
                levelMap.put(cur.right, curNodeLevel + 1);
                queue.add(cur.right);
            }
            if (curNodeLevel == curLevel) {
                curLevelNodes++;
            } else {
                max = Math.max(max, curLevelNodes);
                curLevel++;
                curLevelNodes = 1;
            }
        }
        max = Math.max(max, curLevelNodes);
        return max;
    }
    /**
     * 找到最宽的层树返回
     */
    public static int maxWidthNoMap(Node head) {
        if (head == null) {
            return 0;
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(head);
        Node curEnd = head; // 当前层，最右节点是谁
        Node nextEnd = null; // 下一层，最右节点是谁
        int max = 0;
        int curLevelNodes = 0; // 当前层的节点数
        while (!queue.isEmpty()) {
            Node cur = queue.poll();
            if (cur.left != null) {
                queue.add(cur.left);
                nextEnd = cur.left;
            }
            if (cur.right != null) {
                queue.add(cur.right);
                nextEnd = cur.right;
            }
            curLevelNodes++;
            if (cur == curEnd) {
                max = Math.max(max, curLevelNodes);
                curLevelNodes = 0;
                curEnd = nextEnd;
            }
        }
        return max;
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
