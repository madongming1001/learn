package com.madm.data_structure;

import lombok.Data;

/**
 * @author dongming.ma
 * @date 2023/3/14 01:28
 */
@Data
public class TreeNode {
    public String val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode(String x) {
        val = x;
    }
}