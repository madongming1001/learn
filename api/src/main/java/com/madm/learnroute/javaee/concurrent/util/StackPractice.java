package com.madm.learnroute.javaee.concurrent.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

/**
 * @author dongming.ma
 * @date 2023/9/2 19:39
 */
public class StackPractice {
    public static void main(String[] args) {
        Stack<Integer> intStack = new Stack<>();
        intStack.push(1);
        intStack.pop();
        intStack.add(1);
        intStack.remove(1);
        intStack.elements();
        intStack.peek();
        intStack.search(1);
        intStack.contains(1);
        //LIFO Last input First Output
        Deque<Integer> stack = new ArrayDeque<>();
    }
}
