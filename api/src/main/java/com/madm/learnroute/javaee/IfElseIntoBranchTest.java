package com.madm.learnroute.javaee;

import lombok.extern.slf4j.Slf4j;

/**
 * if else-if短路判断有一个为真后续就不走了
 *
 * @author dongming.ma
 * @date 2023/3/6 12:36
 */
@Slf4j
public class IfElseIntoBranchTest {
    public static void main(String[] args) {
        for (int i = 0; ; i++) {
            boolean condition = true;
            if (!condition) {
                log.info("1");
            } else if (condition) {
                System.out.println("当前id值" + i);
                log.info("2");
            } else if (!condition) {
                log.info("3");
            } else if (condition) {
                System.out.println();
                System.out.println("当前id值" + i);
                break;
            }
            if (condition) {
                log.info("5");
            } else if (condition) {
                log.info("6");
            }
        }
    }
}
