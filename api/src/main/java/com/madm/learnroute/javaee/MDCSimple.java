package com.madm.learnroute.javaee;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.collect.Lists;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.UUID;

/**
 * MDC 全称是 Mapped Diagnostic Context，可以粗略的理解成是一个线程安全的存放诊断日志的容器。
 * MDC 全拼 Mapped Diagnostic Contexts，是SLF4J类日志系统中实现分布式多线程日志数据传递的重要工具（不同的系统有不同的实现方式，下文中会有介绍）；同时，用户也可利用MDC将一些运行时的上下文数据打印出来。
 */

@Slf4j
public class MDCSimple {
    //    private static final log log = logFactory.getlog(SimpleMDC.class);
    public static final String REQ_ID = "REQ_ID";

    public static void main(String[] args) {
//        singleThreadLog();
//        multiThreadLog();
        ArrayList<String> meetingList = Lists.newArrayList("1", "2", "3");
        log.info("save batch meeting OK, size [{}] , data : {}", 1, meetingList);

    }

    private static void multiThreadLog() {
        new BizHandle("F0000").start();
        new BizHandle("F9999").start();
    }

    private static void singleThreadLog() {
        MDC.put(REQ_ID, UUID.randomUUID().toString());
        log.info("开始调用服务A，进行业务处理");
        log.info("业务处理完毕，可以释放空间了，避免内存泄露");
        MDC.remove(REQ_ID);
        log.info("REQ_ID 还有吗？{}", MDC.get(REQ_ID) != null);
    }
}

@Slf4j
class BizHandle extends Thread {


    //    private static final log log = logFactory.getlog(SimpleMDC.class);
    public static final String REQ_ID = "REQ_ID";


    private String funCode;


    public BizHandle(String funCode) {
        this.funCode = funCode;
    }


    @Override
    public void run() {
        MDC.put(REQ_ID, UUID.randomUUID().toString());
        log.info("开始调用服务{}，进行业务处理", funCode);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        }
        log.info("服务{}处理完毕，可以释放空间了，避免内存泄露", funCode);
        MDC.remove(REQ_ID);
    }
}