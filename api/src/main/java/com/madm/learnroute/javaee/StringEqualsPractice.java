package com.madm.learnroute.javaee;


/**
 * -server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly
 */
public class StringEqualsPractice {
    public static void main(String[] args) {
        String abc = new String("abc");
//        String abc1 = new String("abc");
//        System.out.println(abc == abc1);

//        String tags = "{\"name\":\"2\",\"id\":1}";
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
        System.out.println("8个1：" + Integer.valueOf("11111111", 2));//255
        System.out.println("12个1：" + Integer.valueOf("111111111111", 2));//4095
        System.out.println("14个1：" + Integer.valueOf("11111111111111", 2));//16383
        System.out.println("15个1：" + Integer.valueOf("111111111111111", 2));//32767
        System.out.println("16个1：" + Integer.valueOf("1111111111111111", 2));//65535
        //System.out.println("32个1：" + Integer.valueOf(String.valueOf(Integer.MAX_VALUE), 2));//2147483647
        System.out.println(Integer.toBinaryString(2147483647));//反码+补码 绝对值+1
        System.out.println(Integer.toBinaryString(Integer.MIN_VALUE));
//        System.out.println(Integer.toBinaryString(65));//A
//        System.out.println(Integer.toBinaryString(97));//a

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
}

