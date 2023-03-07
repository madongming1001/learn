package com.madm.learnroute.javaee;

import cn.hutool.core.lang.Singleton;
import com.google.common.collect.Lists;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 泛型中 extends 和 super 的区别？
 * <? extends T>和<? super T>是Java泛型中的“通配符（Wildcards）”和“边界（Bounds）”的概念。
 * <p>
 * 表示类型的上界，表示参数化类型的可能是T 或是 T的子类
 * <? extends T>：是指 “上界通配符（Upper Bounds Wildcards）” 只能往外取，不能往里存，读取出来的东西只能存放在Fruit或它的基类里。
 * <p>
 * 表示类型下界（Java Core中叫超类型限定），表示参数化类型是此类型的超类型（父类型），直至Object
 * <? super T>：是指 “下界通配符（Lower Bounds Wildcards）” 只能往里存，可以读取但出来的东西只能存放在Object类里。
 * <p>
 * 参考文章：https://itimetraveler.github.io/2016/12/27/%E3%80%90Java%E3%80%91%E6%B3%9B%E5%9E%8B%E4%B8%AD%20extends%20%E5%92%8C%20super%20%E7%9A%84%E5%8C%BA%E5%88%AB%EF%BC%9F/
 *
 * @author dongming.ma
 * @date 2022/7/4 23:56
 */
public class WildcardPractice {
    public static void main(String[] args) {
        //编译器会认为装苹果的盘子 not is a 装水果的盘子
        //所以，就算容器里装的东西之间有继承关系，但容器之间是没有继承关系的。所以我们不可以把Plate的引用传递给Plate。
//        Plate<Fruit> p=new Plate<Apple>(new Apple());

        Map<String, Fruit> map1 = new HashMap();
        mapPrint1(map1);
        Map<String, List<? extends Fruit>> map2 = new HashMap();
        //同样地，出于对类型安全的考虑，我们可以加入Apple对象或者其任何子类（如RedApple）对象，但由于编译器并不知道List的内容究竟是Apple的哪个超类，因此不允许加入特定的任何超类型。
        List<? super Apple> plates = Lists.newArrayList();
        plates.add(new Apple());
        mapPrint2(map2);
        WildcardPractice singleton = Singleton.get(WildcardPractice.class);

    }

    public static void mapPrint1(Map<String, ? extends Fruit> map1) {
        System.out.println(map1);
    }

    public static void mapPrint2(Map<String, ? extends List<? extends Fruit>> map2) {
        System.out.println(map2);
    }

}

class Fruit {
}

@ToString
class Apple extends Fruit {
}

@NoArgsConstructor
class Plate<T> {
    private T item;

    public Plate(T t) {
        item = t;
    }

    public void set(T t) {
        item = t;
    }

    public T get() {
        return item;
    }
}