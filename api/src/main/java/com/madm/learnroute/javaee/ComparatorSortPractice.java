package com.madm.learnroute.javaee;

import com.madm.learnroute.model.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Comparable 翻译为中文是“比较”的意思，而 Comparator 是“比较器”的意思。
 * Comparable 是以 -able 结尾的，表示它自身具备着某种能力，
 * 而 Comparator 是以 -or 结尾，表示自身是比较的参与者，这是从字面含义先来理解二者的不同。
 *
 * @author dongming.ma
 * @date 2022/7/3 11:07
 */
public class ComparatorSortPractice {
    public static void main(String[] args) {
        List<Employee> employees = getUnsortedEmployeeList();
//
//        Compare by first name and then last name
        Comparator<Employee> compare = Comparator.comparing(Employee::getName).reversed().thenComparing(Employee::getMobile).reversed();

        System.out.println(employees);
        Collections.sort(employees, compare);

        System.out.println(employees);
    }

    private static ArrayList<Employee> getUnsortedEmployeeList() {
        ArrayList<Employee> list = new ArrayList<>();
        list.add(new Employee(2L, "Lokesh"));
        list.add(new Employee(1L, "Alex"));
        list.add(new Employee(4L, "Brian"));
        list.add(new Employee(5L, "Neon"));
        list.add(new Employee(3L, "David"));
        list.add(new Employee(7L, "Alex"));
        list.add(new Employee(6L, "Brian"));
        return list;
    }
}
