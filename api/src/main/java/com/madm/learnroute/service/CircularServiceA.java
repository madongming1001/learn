package com.madm.learnroute.service;


import cn.hutool.extra.spring.SpringUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class CircularServiceA {
    private String fieldA = "字段 A";

    @Autowired
    private CircularServiceB circularServiceB;

    @Transactional
    public void methodA(String a, String b) {
        SqlSessionFactory sqlSessionFactory = (SqlSessionFactory) TransactionSynchronizationManager.getResource(SpringUtil.getBean("sqlSessionFactory"));
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void suspend() {
//
//            }
//        });
        //int i = 1 / 0;//测试 @AfterThrowing 通知
//        System.out.println("方法 A 执行");
    }
}