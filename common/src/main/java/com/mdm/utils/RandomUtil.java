package com.mdm.utils;

import cn.hutool.core.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.util.RandomUtil.randomInt;
import static cn.hutool.core.util.RandomUtil.randomInts;

public class RandomUtil {

    public static int[] createArrayOf(int size) {
        int[] result = new int[size];
        int max = 0;
        for (int i = 0; i < size; i++) {
            result[i] = (int) (Math.random() * size) + 1;
            if (max < result[i]) {
                max = result[i];
            }
        }
        return result;
    }

    /**
     * 生成指定文字数量的集合
     *
     * @param count  文字数量
     * @param length 集合数量
     * @return 指定文字限制的集合
     */
    public static List<String> randomChinese1(int count, int length) {
        if (count * length > 40960 - 19968) {
            throw new IllegalArgumentException("指定单词数量大于最大文字数");
        }
//        //中日韩统一表意文字区段包含了20,992个汉字，编码范围为U+4E00–U+9FFF
        List<String> res = new ArrayList<>();
        final int[] range = ArrayUtil.range('\u4E00', '\ua000');
        int[] indexes = randomInts(range.length);
        StringBuilder str = new StringBuilder(count);
        for (int i = 0; i < count * length; i++) {
            if (i >= count && i % count == 0) {
                res.add(str.toString());
                str.delete(0, str.length());
            }
            str.append((char) range[indexes[i]]);
        }
        if (str.length() != 0) {
            res.add(str.toString());
        }
        return res;
    }

    public static List<String> randomChinese2(int count, int length) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            StringBuilder str = new StringBuilder(count);
            for (int j = 0; j < count; j++) {
                char word = (char) randomInt('\u4E00', '\u9FFF');//\\u是转义字符，表示后面跟一个十六进制数 19968 ~ 40959
                str.append(word);
            }
            res.add(str.toString());
            str.delete(0, str.length());
        }
        return res;
    }

}
