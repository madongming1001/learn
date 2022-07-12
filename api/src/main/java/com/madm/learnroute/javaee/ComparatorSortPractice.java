package com.madm.learnroute.javaee;

import com.madm.learnroute.model.Employee;
import com.mdm.utils.GsonObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author dongming.ma
 * @date 2022/7/3 11:07
 */
public class ComparatorSortPractice {
    public static void main(String[] args) {
        List<Employee> employees = getUnsortedEmployeeList();
//
//        Compare by first name and then last name
        Comparator<Employee> compareByName = Comparator.comparing(Employee::getName).thenComparing(Employee::getSalary);

        System.out.println(GsonObject.createGson().toJson(employees));
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
