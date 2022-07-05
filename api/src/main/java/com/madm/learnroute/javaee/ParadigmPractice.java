package com.madm.learnroute.javaee;

/**
 * 泛型中 extends 和 super 的区别？
 * <? extends T>和<? super T>是Java泛型中的“通配符（Wildcards）”和“边界（Bounds）”的概念。
 *
 * 表示类型的上界，表示参数化类型的可能是T 或是 T的子类
 * <? extends T>：是指 “上界通配符（Upper Bounds Wildcards）” 不能往里存，只能往外取，读取出来的东西只能存放在Fruit或它的基类里。
 *
 * 表示类型下界（Java Core中叫超类型限定），表示参数化类型是此类型的超类型（父类型），直至Object
 * <? super T>：是指 “下界通配符（Lower Bounds Wildcards）” 不影响往里存，但往外取只能放在Object对象里，读取出来的东西只能存放在Object类里。
 *
 * 参考文章：https://itimetraveler.github.io/2016/12/27/%E3%80%90Java%E3%80%91%E6%B3%9B%E5%9E%8B%E4%B8%AD%20extends%20%E5%92%8C%20super%20%E7%9A%84%E5%8C%BA%E5%88%AB%EF%BC%9F/
 * @author dongming.ma
 * @date 2022/7/4 23:56
 */
public class ParadigmPractice {
    public static void main(String[] args) {
        //编译器会认为装苹果的盘子 not is a 装水果的盘子
        //所以，就算容器里装的东西之间有继承关系，但容器之间是没有继承关系的。所以我们不可以把Plate的引用传递给Plate。
//        Plate<Fruit> p=new Plate<Apple>(new Apple());
    }
}
class Fruit {}
class Apple extends Fruit {}
class Plate<T>{
    private T item;
    public Plate(T t){item=t;}
    public void set(T t){item=t;}
    public T get(){return item;}
}