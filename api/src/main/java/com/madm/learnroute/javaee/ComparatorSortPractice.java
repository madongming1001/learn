package com.madm.learnroute.javaee;

import com.mdm.springfeature.entity.Employee;
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
        Comparator<Employee> compareByName = Comparator.comparing(Employee::getFirstName).thenComparing(Employee::getLastName);

        System.out.println(GsonObject.createGson().toJson(employees));
    }

    private static ArrayList<Employee> getUnsortedEmployeeList() {
        ArrayList<Employee> list = new ArrayList<>();
        list.add(new Employee(2L, "Lokesh", "Gupta"));
        list.add(new Employee(1L, "Alex", "Gussin"));
        list.add(new Employee(4L, "Brian", "Sux"));
        list.add(new Employee(5L, "Neon", "Piper"));
        list.add(new Employee(3L, "David", "Beckham"));
        list.add(new Employee(7L, "Alex", "Beckham"));
        list.add(new Employee(6L, "Brian", "Suxena"));
        return list;
    }
}
