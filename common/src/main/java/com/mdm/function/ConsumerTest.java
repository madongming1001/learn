package com.mdm.function;

import java.util.function.Consumer;

/**
 * 消费形接口，有参数，无返回值
 */
public class ConsumerTest {

//    public static void main(String[] args) {
//        summer(10000, m -> System.out.println("世界那么大，我想去看看，可是钱包仅有：" + m + "元"));
//    }

    public static void summer(double money, Consumer<Double> con) {
        con.accept(money);
    }
}
