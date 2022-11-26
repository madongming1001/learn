package com.madm.leetcode;

import java.util.Stack;

/**
 * 678. 有效的括号字符串
 * 给定一个只包含三种字符的字符串：（，）和 *，写一个函数来检验这个字符串是否为有效字符串。有效字符串具有如下规则：
 * <p>
 * 任何左括号 (必须有相应的右括号 )。
 * 任何右括号 )必须有相应的左括号 (。
 * 左括号 ( 必须在对应的右括号之前 )。
 * *可以被视为单个右括号 )，或单个左括号 (，或一个空字符串。
 * 一个空字符串也被视为有效字符串。
 * 示例 1:
 * <p>
 * 输入: "()"
 * 输出: True
 * 示例 2:
 * <p>
 * 输入: "(*)"
 * 输出: True
 * 示例 3:
 * <p>
 * 输入: "(*))"
 * 输出: True
 *
 * @author dongming.ma
 * @date 2022/11/17 16:46
 */
public class Topic_678 {
    public static boolean checkValidString(String s) {
        Stack<Integer> left = new Stack<>(), star = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char character;
            if ((character = s.charAt(i)) == '(') left.push(i);
            if (character == '*') star.push(i);
            if (character == ')') {
                if (left.size() == 0) {
                    if (star.size() == 0) return false;
                    star.pop();
                } else {
                    left.pop();
                }
            }
        }
        if (left.size() > star.size()) return false;
//        while (left.size() != 0 && star.size() != 0) {
//            if (left.pop() > star.pop()) return false;
//        }
        return true;
    }

    public static void main(String[] args) {
        if (Topic_678.checkValidString("()")) {
            System.out.println("匹配");
        }
    }
}
