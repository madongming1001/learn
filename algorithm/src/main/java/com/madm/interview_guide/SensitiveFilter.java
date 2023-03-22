package com.madm.interview_guide;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author dongming.ma
 * @date 2023/3/22 18:20
 */
public class SensitiveFilter {
    private static HashMap<Object, Object> sensitiveWordMap = null;

    public static void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap<>(keyWordSet.size());
        String key = null;
        Map nowMap = null;
        Map<String, String> newWordMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    newWordMap = new HashMap<>();
                    newWordMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWordMap);
                    nowMap = newWordMap;
                }
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    public static int checkSensitiveWord(String text, int beginIndex, int matchType) {
        boolean flag = false;
        int matchFlag = 0;
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < text.length(); i++) {
            word = text.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    //if(SensitiveWordFilter.minMatchType == matchType)break;
                }
            } else {
                break;
            }
        }
        if (matchFlag < 2 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;

    }
}
