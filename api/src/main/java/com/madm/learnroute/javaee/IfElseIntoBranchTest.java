package com.madm.learnroute.javaee;

import lombok.extern.slf4j.Slf4j;

/**
 * if else-if短路判断有一个为真后续就不走了
 * @author dongming.ma
 * @date 2023/3/6 12:36
 */
@Slf4j
public class IfElseIntoBranchTest {
    public static void main(String[] args) {
        boolean condition = true;
        if (!condition) {
            log.info("1");
        } else if (condition) {
            log.info("2");
        } else if (!condition) {
            log.info("3");
        } else if (condition) {
            log.info("4");
        }

        if (condition){
            log.info("5");
        } else if (condition) {
            log.info("6");
        }
    }
}
