package com.madm.learnroute.concurrency.juc;

import net.bytebuddy.agent.builder.AgentBuilder;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * -XX:+PrintCompilation 在控制台打印编译过程信息
 * -XX:+UnlockDiagnosticVMOptions 解锁对jvm进行诊断的选项参数 默认是关闭的 开启后支持特定的一些参数对JVM进行诊断
 * -XX:+PrintAssembly
 * -Xcomp
 *
 * @author dongming.ma
 * @date 2022/5/30 19:13
 */
@ThreadSafe
public class VolatileCachedFactorizer {

    private volatile boolean volflag = true;

    public static void main(String[] args) {}

}
