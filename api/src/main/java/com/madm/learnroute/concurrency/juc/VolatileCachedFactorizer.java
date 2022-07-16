package com.madm.learnroute.concurrency.juc;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * -XX:+UnlockDiagnosticVMOptions
 * -XX:+PrintAssembly -Xcomp
 *
 * @author dongming.ma
 * @date 2022/5/30 19:13
 */
@ThreadSafe
public class VolatileCachedFactorizer {

    private volatile boolean volflag = true;

    public static void main(String[] args) {
        System.out.println("Sss");
    }

}
