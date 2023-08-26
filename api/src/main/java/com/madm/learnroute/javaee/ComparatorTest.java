package com.madm.learnroute.javaee;


import com.madm.learnroute.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author dongming.ma
 * @date 2022/7/3 22:58
 */
public class ComparatorTest {
    public static void main(String[] args) {
        Employee e1 = new Employee("John", 25, 3000, 9922001);
        Employee e2 = new Employee("Ace", 22, 2000, 5924001);
        Employee e3 = new Employee("Keith", 35, 4000, 3924401);
        List<Employee> employees = new ArrayList<>();
        employees.add(e1);
        employees.add(e2);
        employees.add(e3);

        /**
         *     @SuppressWarnings({"unchecked", "rawtypes"})
         *     default void sort(Comparator<? super E> c) {
         *         Object[] a = this.toArray();
         *         Arrays.sort(a, (Comparator) c);
         *         ListIterator<E> i = this.listIterator();
         *         for (Object e : a) {
         *             i.next();
         *             i.set((E) e);
         *         }
         *     }
         *
         *     sort 对象接收一个 Comparator 函数式接口，可以传入一个lambda表达式
         */
        employees.sort(Comparator.comparing(Employee::getName));

        Collections.sort(employees, Comparator.comparing(Employee::getName));

        employees.forEach(System.out::println);


        /**
         * Comparator.comparing 方法的使用
         *
         * comparing 方法接收一个 Function 函数式接口 ，通过一个 lambda 表达式传入
         *
         */
        employees.sort(Comparator.comparing(e -> e.getName()));
        /**
         * 该方法引用 Employee::getName 可以代替 lambda表达式
         */
        employees.sort(Comparator.comparing(Employee::getName));

        Comparator<Integer> customComparator = Comparator.comparingInt(o -> o);
    }
}
