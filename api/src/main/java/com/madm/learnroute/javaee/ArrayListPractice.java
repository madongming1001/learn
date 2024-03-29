package com.madm.learnroute.javaee;

import com.mdm.pojo.User;

import java.util.*;

/**
 * 线程不安全 扩容为1.5倍
 * Arraylist 与 LinkedList 区别?
 * 1、是否保证线程安全： ArrayList 和 LinkedList 都是不同步的，也就是不保证线程安全；
 * 2、底层数据结构： Arraylist 底层使用的是 Object 数组；LinkedList 底层使用的是 双向链表 数据结构（JDK1.6 之前为循环链表，JDK1.7 取消了循环。注意双向链表和双向循环链表的区别，下面有介绍到！）
 * 3、插入和删除是否受元素位置的影响：
 * ① ArrayList 采用数组存储，所以插入和删除元素的时间复杂度受元素位置的影响。 比如：执行add(E e)方法的时候， ArrayList 会默认将指定的元素追加到此列表的末尾，这种情况时间复杂度就是 O(1)。但是如果要在指定位置 i 插入和删除元素的话（add(int index, E element)）时间复杂度就为 O(n-i)。因为在进行上述操作的时候集合中第 i 和第 i 个元素之后的(n-i)个元素都要执行向后位/向前移一位的操作。
 * ② LinkedList 采用链表存储，所以对于add(E e)方法的插入，删除元素时间复杂度不受元素位置的影响，近似 O(1)，如果是要在指定位置i插入和删除元素的话（(add(int index, E element)） 时间复杂度近似为o(n))因为需要先移动到指定位置再插入。
 * 4、是否支持快速随机访问： LinkedList 不支持高效的随机元素访问，而 ArrayList 支持。快速随机访问就是通过元素的序号快速获取元素对象(对应于get(int index)方法)。
 * 5、内存空间占用： ArrayList 的空间浪费主要体现在在 list 列表的结尾会预留一定的容量空间，而 LinkedList 的空间花费则体现在它的每一个元素都需要消耗比 ArrayList 更多的空间（因为要存放直接后继和直接前驱以及数据）。
 * <p>
 * 基本类型、包装类型、引用类型、String等作为实参传递后值不会改变
 * 但是修改对象地址的原值会成功 比如user.setid就会影响外部方法的对象值
 * 如果是用他的实例对象new了 那就不会影响外部的值
 *
 * @author dongming.ma
 * @date 2022/7/5 14:24
 */
public class ArrayListPractice {
    public static void main(String[] args) {

//        ArrayList<String> list = new ArrayList<>();
//        list.add("11");
//        System.out.println(list);
        ArrayListPractice ap = new ArrayListPractice();
        ap.ensureCapacity();

    }

    public void ensureCapacity() {
        User u1 = new User("u1");
        User u2 = new User("u2");
        User u3 = new User("u3");
        User u4 = new User("u4");
        User u5 = new User("u5");
        User u6 = new User("u6");
        User u7 = new User("u7");
        User u8 = new User("u8");
        User u9 = new User("u9");


//        changeValue(u1, u2);
//        System.out.println(u1 + "\n" + u2);
        HashMap<Object, String> userMaps = new HashMap<>(64);
        Map<Object, String> safeUserMaps = Collections.synchronizedMap(userMaps);
        userMaps.put(u1, "u1");
        userMaps.put(u2, "u2");
        userMaps.put(u3, "u3");
        userMaps.put(u4, "u4");
        userMaps.put(u5, "u5");
        userMaps.put(u6, "u6");
        userMaps.put(u7, "u7");
        userMaps.put(u8, "u8");
        userMaps.put(u9, "u9");

        userMaps.put(new Apple(), "apple");
        userMaps.put(new BlueApple(), "blueapple");
        userMaps.put(new RedApple(), "redapple");
        Set<Object> appleSet = userMaps.keySet();
        Iterator<Object> appIterator = appleSet.iterator();
        while (appIterator.hasNext()) {
            Object next = appIterator.next();
            System.out.println(next);
        }

        userMaps.forEach((k, v) -> System.out.println(k + v));


        LinkedList linkedList = new LinkedList();
        ArrayListHandler handler = new ArrayListHandler();
        ArrayList<Object> list = new ArrayList();
        list.add(UUID.randomUUID().toString());
        list.add(UUID.randomUUID().toString());
        list.add(UUID.randomUUID().toString());
        list.add(UUID.randomUUID().toString());
        list.add(UUID.randomUUID().toString());
        list.add(UUID.randomUUID().toString());
        list.add(UUID.randomUUID().toString());
        list.contains("!");
        list.forEach(handler::register);

        Set<String> sets = handler.map.keySet();
        Iterator<String> iterator = sets.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            System.out.println(next);
        }

        final int N = 10000000;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("使用ensureCapacity方法前：" + (endTime - startTime));

        list.clear();


        long startTime1 = System.currentTimeMillis();
        list.ensureCapacity(N);
        for (int i = 0; i < N; i++) {
            list.add(i);
        }
        long endTime1 = System.currentTimeMillis();
        System.out.println("使用ensureCapacity方法后：" + (endTime1 - startTime1));
    }

    public void changeValue(User u1, User u2) {
        u1.setId(1111);

        u2 = new User();
        u2.setId(1111);
    }

    private class ArrayListHandler {

        private Map<String, Object> map = new HashMap();

        private void register(Object obj) {
            String simpleStr = obj.toString().replaceAll("-", "");
            map.put(simpleStr, simpleStr);
        }
    }

}