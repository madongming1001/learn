package com.madm.design.visitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMain {
    public static void main(String[] args) {
        DataView dataView = new DataView();
        log.info(System.lineSeparator() + "家长视角访问：");
        // 家长
        dataView.show(new Parent());
        log.info(System.lineSeparator() + "校长视角访问：");
        // 校长
        dataView.show(new Principal());
    }
}