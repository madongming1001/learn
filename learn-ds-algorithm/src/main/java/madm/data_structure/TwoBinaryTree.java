package madm.data_structure;


import lombok.Data;

import java.util.LinkedList;
import java.util.Objects;

public class TwoBinaryTree {

    @Data
    static class TreeNode<T> {
        T data;
        static TreeNode leftChild;
        static TreeNode rightChild;

        public TreeNode(T data) {
            this.data = data;
        }

    }


    public static TreeNode createBinaryTree(LinkedList<Integer> inputList) {
        TreeNode node = null;
        if (inputList == null || inputList.isEmpty()) {
            return null;
        }
        Integer data = inputList.removeFirst();
        if (data != null) {
            node = new TreeNode<Integer>(data);
            node.leftChild = createBinaryTree(inputList);
            node.rightChild = createBinaryTree(inputList);
        }
        return node;
    }

    public static void preOrderTraveral(TreeNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        System.out.println(node.data);
        preOrderTraveral(node.leftChild);
        preOrderTraveral(node.rightChild);
    }

    public static void inOrderTraveral(TreeNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        preOrderTraveral(node.leftChild);
        System.out.println(node.data);
        preOrderTraveral(node.rightChild);
    }

    public static void postOrderTraveral(TreeNode node) {
        if (Objects.isNull(node)) {
            return;
        }
        preOrderTraveral(node.leftChild);
        preOrderTraveral(node.rightChild);
        System.out.println(node.data);
    }


}
