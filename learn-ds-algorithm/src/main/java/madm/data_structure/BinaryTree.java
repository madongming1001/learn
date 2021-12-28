package madm.data_structure;


import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

public class BinaryTree {
    private Node head;

    static class Node<T> {
        T value;
        public Node left;
        public Node right;

        public Node(T value) {
            this.value = value;
        }

    }


    public static Node createBinaryTree(LinkedList<Integer> inputList) {
        Node node = null;
        if (inputList == null || inputList.isEmpty()) {
            return null;
        }
        Integer data = inputList.removeFirst();
        if (data != null) {
            node = new Node<Integer>(data);
            node.left = createBinaryTree(inputList);
            node.right = createBinaryTree(inputList);
        }
        return node;
    }

    public static void preOrderTraveral(Node node) {
        if (Objects.isNull(node)) {
            return;
        }
        System.out.println(node.value);
        preOrderTraveral(node.left);
        preOrderTraveral(node.right);
    }

    public static void inOrderTraveral(Node node) {
        if (Objects.isNull(node)) {
            return;
        }
        preOrderTraveral(node.left);
        System.out.println(node.value);
        preOrderTraveral(node.right);
    }

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

}
