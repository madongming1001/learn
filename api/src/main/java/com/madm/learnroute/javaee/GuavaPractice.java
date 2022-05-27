package com.madm.learnroute.javaee;

import com.google.common.collect.HashBasedTable;

import java.util.Collection;
import java.util.Set;

/**
 * @Author MaDongMing
 * @Date 2022/3/29 2:04 PM
 */
public class GuavaPractice {
    public static void main(String[] args) {
        HashBasedTable<String, String, Integer> table = HashBasedTable.create();
        //存放元素
        table.put("Hydra", "Jan", 20);
        table.put("Hydra", "Feb", 28);

        table.put("Trunks", "Jan", 28);
        table.put("Trunks", "Feb", 16);

        //取出元素
        Integer dayCount = table.get("Hydra", "Feb");
        System.out.println(dayCount);

        //rowKey或columnKey的集合
        Set<String> rowKeys = table.rowKeySet();
        Set<String> columnKeys = table.columnKeySet();
        //value集合
        Collection<Integer> values = table.values();
    }

}
