package com.madm.learnroute.javaee;


import cn.hutool.core.util.IdUtil;
import com.mdm.utils.PrintUtil;
import org.junit.Assert;

import java.math.BigInteger;

/**
 * -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly
 */
public class StringEqualsPractice {
    public static void main(String[] args) {
        String abc = new String("abc");
//        String abc1 = new String("abc");
//        System.out.println(abc == abc1);

        String tags = "{\"name\":\"2\",\"id\":1}";
//        Map map = new HashMap();
//        if (StringUtils.isNotEmpty(tags)) {
//            map = JSONObject.parseObject(tags,HashMap.class);
//        }
//        System.out.println(map);
//        String str = "null";
//        if(Objects.isNull(str)){
//            System.out.println("结果是null");
//        }
//        System.out.println();
//
//        System.out.println(1 & -1);
//        System.out.println(2 & -2);
//        System.out.println(3 & -3);
//        System.out.println(4 & -4);
//        System.out.println(Integer.toBinaryString(1));
//        System.out.println(Integer.toBinaryString(-1));
//        System.out.println(Integer.toBinaryString(-2));
//        System.out.println(Integer.toBinaryString(-3));

//        System.out.println(abc1 == abc.intern());
//        System.out.println(Long.parseLong("0x7fffffff"));
        //assert (10 - 1) >> 1 == 20; 在正式运行时禁用
        PrintUtil.printSplitLine();
        //Assert.equals((10 - 1) >> 1, 4);
        cn.hutool.core.lang.Assert.isNull(null);
        Assert.assertEquals((10 - 1) >> 1, 4);

        // 这个就是那个音符字符，只不过由于当前的网页没支持这种编码，所以没显示。
        // 这个就是音符字符的UTF-16编码
        // 代码单元指一种转换格式（UTF）中最小的一个分隔，称为一个代码单元（Code Unit）
        String B = "𝄞";
        String C = "\uD834\uDD1E";
        System.out.println(C);
        System.out.println(B.length());
        System.out.println(B.codePointCount(0, B.length()));

        System.out.println(Integer.toBinaryString(4));
        System.out.println(Integer.toBinaryString(-4));

//        System.out.println(Integer.valueOf("11111",2));
        System.out.println(Integer.valueOf("11111", 2));
        System.out.println("65535  二进制：" + Integer.toBinaryString(65535));
        System.out.println("32767  二进制：" + Integer.toBinaryString(32767));
        System.out.println("4个1：" + Integer.valueOf("1111", 2));//15
        System.out.println("5个1：" + Integer.valueOf("11111", 2));//31
        System.out.println("6个1：" + Integer.valueOf("111111", 2));//63
        System.out.println("7个1：" + Integer.valueOf("1111111", 2));//127
        System.out.println("8个1：" + Integer.valueOf("11111111", 2));//255
        System.out.println("12个1：" + Integer.valueOf("111111111111", 2));//4095
        System.out.println("13个1：" + Integer.valueOf("1111111111111", 2));//8191
        System.out.println("14个1：" + Integer.valueOf("11111111111111", 2));//16383
        System.out.println("15个1：" + Integer.valueOf("111111111111111", 2));//32767
        System.out.println("16个1：" + Integer.valueOf("1111111111111111", 2));//65535
        System.out.println("获取最高位1之前有多少个0：" + Integer.numberOfLeadingZeros(31));
        System.out.println(Integer.bitCount(16));
        //System.out.println("32个1：" + Integer.valueOf(String.valueOf(Integer.MAX_VALUE), 2));//2147483647
        System.out.println(Integer.toBinaryString(2147483647));//反码+补码 绝对值+1
        System.out.println(Integer.toBinaryString(Integer.MAX_VALUE));
        System.out.println(Integer.MAX_VALUE);
//        System.out.println(Integer.toBinaryString(65));//A
//        System.out.println(Integer.toBinaryString(97));//a

        //System.out.println(Float.floatToIntBits(234));
        System.out.println(Long.MAX_VALUE + " long maxvalue"); //2^63-1.
        System.out.println(BigInteger.valueOf(1l).pow(63));
        System.out.println(IdUtil.fastUUID());
        System.out.println(IdUtil.fastSimpleUUID());


        System.out.println((1 << 48) + " sdfsfsdf s");
        System.out.println(Integer.toBinaryString(0xffff << 16));
        System.out.println(Integer.toBinaryString(1 << 16));
        System.out.println(Integer.toBinaryString(7 >> 16));

        System.out.println(tableSizeFor(7));
        System.out.println(tableSizeFor(31));

        System.out.println(Float.MIN_VALUE);
        System.out.println(Float.MAX_VALUE);

        System.out.println(Double.MIN_VALUE);
        System.out.println(Double.MIN_VALUE);
        float fl = 1.234556672222222222f;

    }


    /**
     * 判断一个数是不是2的幂
     *
     * @param val
     * @return
     */
    private static boolean isPowerOfTwo(int val) {
        /**
         * val： -4
         * positive number： 100
         * negative number： 11111111111111111111111111111100
         */
//        return (val & -val) == val;
        return (val & (val - 1)) == 0;
    }

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= Integer.MAX_VALUE) ? Integer.MAX_VALUE : n + 1;
    }
}

