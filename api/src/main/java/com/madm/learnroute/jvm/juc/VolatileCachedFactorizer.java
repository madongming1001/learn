package com.madm.learnroute.jvm.juc;

import javax.annotation.concurrent.ThreadSafe;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author dongming.ma
 * @date 2022/5/30 19:13
 */
@ThreadSafe
public class VolatileCachedFactorizer {
    private volatile OneValueCache cache = new OneValueCache(null,null);

    public void service(ServletRequest req, ServletResponse resp){
//        BigInteger i = extractFromRequest(req);
//        BigInteger[] factors = cache.getFactors(i);
//        if(factors == null){
//            factors = factor(i);
//            cache = new OneValueCache(i,factors);
//        }
//        encodeIntoResponse(resp,factors);
    }
}
