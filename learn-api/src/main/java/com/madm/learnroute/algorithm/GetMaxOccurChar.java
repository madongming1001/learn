package com.madm.learnroute.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 输出频率最高且最先出现的字符
 */
public class GetMaxOccurChar {

    public static void main(String[] args) {
        System.out.println(Objects.equals(new Long(1),new Long(2)));
//        char result = getMaxOccurChar("hello world, every body!");
//        System.out.println(result);
        char result = getMaxOccurChar02("aaabbbbcccc");
        System.out.println(result);
        LinkedHashMap lh = new LinkedHashMap();
        lh.put("1",1);


    }

    private static char getMaxOccurChar02(String str) {
        int maxCount = 1;
        char result = str.charAt(0);
        LinkedHashMap<Character, Integer> map = new LinkedHashMap<>();
        for(int i = str.length() - 1;i >= 0;i--){
            Integer count = map.getOrDefault(str.charAt(i), 0);
            map.put(str.charAt(i),count + 1);
            if(count >= maxCount){
                maxCount = count;
                result = str.charAt(i);
            }
        }
        return result;
    }


}
