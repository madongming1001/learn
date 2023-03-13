package com.madm.interview_guide;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import com.madm.data_structure.TreeNode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author dongming.ma
 * @date 2023/3/14 01:28
 */
public class TreeNodeShow {
    public static void main(String[] args) {
        TreeNode root = TreeNodeUtil.arrayToTreeNode(Arrays.stream(RandomUtil.randomInts(35)).boxed().toArray(Integer[]::new));
        TreeNodeShow.show(root);
    }

    private static int getTreeDepth(TreeNode root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth((root.left)), getTreeDepth(root.right)));
    }

    private static void writeArray(TreeNode currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currNode == null) return;
        // 先将当前节点保存到二维数组中
        res[rowIndex][columnIndex] = currNode.val.toString();

        // 计算当前位于树的第几层
        int currLevel = ((rowIndex + 1) / 2);
        // 若到了最后一层，则返回
        if (currLevel == treeDepth) return;
        // 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
        int gap = treeDepth - currLevel - 1;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
        if (currNode.left != null && currNode.left.val != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currNode.left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
        if (currNode.right != null && currNode.right.val != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currNode.right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }


    public static void show(TreeNode root) {
        if (root == null) {
            System.out.println("EMPTY!");
            return;
        }
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        int arrayHeight = treeDepth * 2 - 1;
        // 最后一行的长度为2的（n - 1）次方乘3，再加1
        // 作为整个二维数组的长度


        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
//        int arrayWidth = (1 << (treeDepth - 1)) * treeDepth + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (String[] resp : res) {
            Arrays.fill(resp, " ");
        }

        // 从根节点开始，递归处理整个树
        // res[0][(arrayWidth + 1)/ 2] = (char)(root.val + '0');
        writeArray(root, 0, arrayWidth / 2, res, treeDepth);

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        for (String[] line : res) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length; i++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2 : line[i].length() - 1;
                }
            }
            System.out.println(sb);
        }
    }
}

class TreeNodeUtil {
    public static TreeNode arrayToTreeNode(Integer[] arr) {
        String[] strs = Arrays.stream(arr).map(obj -> obj == null ? null : obj.toString()).toArray(String[]::new);
        TreeNode root = new TreeNode(strs[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean isLeft = true;
        for (int i = 1; i < arr.length; i++) {
            TreeNode node = queue.peek();
            if (isLeft) {
                node.left = new TreeNode(strs[i]);
                queue.offer(node.left);
                isLeft = false;
            } else {
                node.right = new TreeNode(strs[i]);
                queue.offer(node.right);
                queue.poll();
                isLeft = true;
            }
        }
        return root;
    }
}

