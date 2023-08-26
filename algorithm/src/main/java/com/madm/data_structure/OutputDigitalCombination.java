package com.madm.data_structure;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class OutputDigitalCombination {
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String[] arr = new String[]{
                "1", "2", "3", "4"
        };
        listAll(Arrays.asList(arr), " ");

    }

    public static void listAll(List<String> candidate, String prefix) {
        System.out.println(prefix);

        for (int i = 0; i < candidate.size(); i++) {
            List<String> temp = new LinkedList<String>(candidate);//new LinkedList<String>(candidate)---copy candidate
            listAll(temp, prefix + temp.remove(i));
        }
    }
}
