package com.madm.learnroute.javaee;

/**
 * @author dongming.ma
 * @date 2022/10/2 17:26
 */
public class MathPractice {
    public static void main(String[] args) {
        //返回最小大于给定数值的整数 向上取整
        System.out.println(Math.ceil(11.2));
        //返回最大小于给定数值的整数 向下取整
        System.out.println(Math.floor(11.2));
        //返回数字四舍五入的整数
        System.out.println(Math.round(11.2));
        //用来求 x 的 y 次幂（次方），其原型为：
        System.out.println(Math.pow(2, 63));
        //参数小于等于0返回参数本身 参数无穷大就是无穷大 否则返回最接近真正的数学平方根的参数值的double值。
        System.out.println(Math.sqrt(5));
        //求平均值
        System.out.println(Math.abs(-41));
        //求最小值
        System.out.println(Math.min(-1, -2));
        System.out.println(Math.exp(2));
        System.out.println(Math.log(9.223372036854776E18));//默认是e为底数 e = 2.71828182846

    }
}