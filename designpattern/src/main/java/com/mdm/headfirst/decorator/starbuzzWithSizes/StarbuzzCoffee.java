package com.mdm.headfirst.decorator.starbuzzWithSizes;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.stream.Collectors;

public class StarbuzzCoffee {

    public static final String USER_INFO_STRING = "userInfo:uid_%s";
    private static final String BASE_KEY = "mallchat:";

    public static void main(String args[]) {
        List<String> uids = Lists.newArrayList("1", "2", "3", "4", "5", "6");
        List<String> keys = uids.stream().map(a -> StarbuzzCoffee.getKey(StarbuzzCoffee.USER_INFO_STRING, a)).collect(Collectors.toList());
        System.out.println(keys);


        Beverage beverage = new Espresso();
        System.out.println(beverage.getDescription() + " $" + String.format("%.2f", beverage.cost()));

        System.out.println(beverage.getDescription() + " $" + String.format("%.2f", beverage.cost()));

        Beverage beverage2 = new DarkRoast();//.99
        beverage2 = new Mocha(beverage2);//.20
        beverage2 = new Mocha(beverage2);//.20
        beverage2 = new Whip(beverage2);//.10
        System.out.println(beverage2.getDescription() + " $" + String.format("%.2f", beverage2.cost()));

        Beverage beverage3 = new HouseBlend();
        beverage3.setSize(Beverage.Size.VENTI);
        beverage3 = new Soy(beverage3);
        beverage3 = new Mocha(beverage3);
        beverage3 = new Whip(beverage3);
        System.out.println(beverage3.getDescription() + " $" + String.format("%.2f", beverage3.cost()));
    }

    public static String getKey(String key, Object... objects) {
        return BASE_KEY + String.format(key, objects);
    }
}
