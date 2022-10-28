package com.madm.learnroute.javaee.concurrency.juc;

import org.bouncycastle.util.Arrays;

import java.math.BigInteger;

/**
 * @author dongming.ma
 * @date 2022/5/30 19:11
 */
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger lastNumber, BigInteger[] lastFactors) {
        this.lastNumber = lastNumber;
        this.lastFactors = Arrays.copyOf(lastFactors,lastFactors.length);
    }

    public BigInteger[] getFactors(BigInteger i){
        if(lastNumber == null || !lastNumber.equals(i)){
            return null;
        }
        return Arrays.copyOf(lastFactors,lastFactors.length);
    }
}
