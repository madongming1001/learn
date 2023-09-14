package com.madm.interview_guide;

import com.madm.sort.MergeSort;
import com.mdm.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 有一个10G的文件，里面有乱序的数字，现只提供1G内存
 * 1.如何将这个文件中的所有数字排序？
 * 2.求文件中出现次数最多的数字的次数
 * <p>
 * https://www.bilibili.com/video/BV17B4y1W7jC
 *
 *  1、快速排序法 时间复杂度O(nlogn)
 *  2、淘汰法 时间复杂度O(n + m^z)
 *  3、分支法 数据分若干份 可以利用多线程没份单独排序找到单个文件前多少个最大的 最后在剩下的里面在归并排序寻找
 *  4、最小堆算法 根据条件创建最小堆 时间复杂度 O(nmlogm) 空间复杂度常数 m是规定的大小
 */
@Slf4j
public class BiggestFileSort {

    private static final Map<String, Object> cache = new ConcurrentHashMap();


    public static void main(String[] args) {
        // 1.获取数据源
        int[] sourcesData = RandomUtil.createArrayOf(1000_0000);
        // 2.开始排序
        long begin = System.currentTimeMillis();
        Integer[] processor = BiggestFileSort.processor(sourcesData, 2);
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
        List<Integer> collect = Arrays.stream(processor).collect(Collectors.toList());
        System.out.println(collect);
    }

    private static Integer[] processor(int[] sourcesData, int partition) {
        int index = partition;
        Integer[] nextArr = new Integer[partition];

        // 1.结果集初始化
        Integer[] result = new Integer[partition];
        for (Integer i = 0; i < partition; i++) {
            result[i] = sourcesData[i];
        }
        MergeSort.group(Arrays.stream(result).mapToInt(Integer::intValue).toArray());
        // 2.分段比较开始
        while (index < sourcesData.length) {
            for (Integer i = 0; i < partition; i++) {
                nextArr[i] = sourcesData[index++];
            }
            merge(result, nextArr, partition);
        }
        return result;
    }

    public static void merge(Integer[] leftArr, Integer[] rightArr, Integer partition) {
        System.out.println(leftArr);
        // 1.中转数组
        Integer[] help = new Integer[partition];
        int leftIndex = 0;
        int rightIndex = 0;
        int index = 0;
        MergeSort.group(Arrays.stream(rightArr).mapToInt(Integer::intValue).toArray());
        while ((leftIndex < partition) && (rightIndex < partition) && (index < partition)) {
            help[index++] = leftArr[leftIndex] < rightArr[rightIndex] ? rightArr[rightIndex++] : leftArr[leftIndex++];
        }
//         for (Integer i = 0; i < partition; i++) {
//             leftArr[i]=help[i];
//         }
        leftArr = help;
    }

    /**
     * 1.
     * 先遍历一遍文件，找到所有数字的区间范围
     * 将范围划分成15份的小区间，也就是15个桶
     * 再次遍历文件
     * readLine读取每一行，根据数字大小计算自己属于哪个区间，将其写入桶所在的小文件中
     * 依次遍历每个小文件，将小文件中的所有数读入内存，进行排序
     * 最终每个小文件皆有序
     * 将15个小文件拼接起来
     *
     * 追求绝对的速度->快排
     * 追求空间节省->堆排 空间复杂度O(1) 省空间，时间复杂度O(N*logN)
     * 追求稳定性->归并
     */

    /**
     * 2.
     * 文件10G，int类型4字节，总数字数=10*1024*1024*1024/4 = 26.84亿
     * 使用 HashMap<Integer, Integer>记录每个数字出现的次数，所以每存一条K-V占4+4=8byte
     * 考虑最差情况下，10G文件中所有数字都不同 -> 1G内存最多存的K-V数 = 1024*1024*1024/8 = 1.34亿
     * 分桶数 = 26.84/1.34 = 20.03
     * 保守分桶数 = 25
     * int类型4字节，能表示的数字范围: [-2^31, 2^31], 即[-21.47亿, 21.47亿]
     * <p>
     * 读文件readLine...得到数字num
     */
    public int getMostNum() throws Exception {
        int res = 0;
        HashMap<Integer, Integer> map = new HashMap<>();
        /**
         * 25个小文件输出流
         */
        HashMap<Integer, OutputStreamWriter> writers = new HashMap<>();
        for (int i = 0; i < 25; i++) {
            writers.put(i, new OutputStreamWriter(new FileOutputStream("split file:" + i)));
        }

        /**
         * 读取一个数字，模25得到桶下标
         * 写入对应的小文件中
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("10G File Path")));
        String x;
        while ((x = reader.readLine()) != null) {
            int bucketIndex = Integer.parseInt(x) % 25;
            writers.get(bucketIndex).write(x);
        }

        /**
         * 针对每个小文件遍历，累计次数
         */
        for (int i = 0; i < 25; i++) {
            BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream("split file:" + i)));
            String xx;
            while ((xx = reader1.readLine()) != null) {
                Integer k1 = Integer.valueOf(xx);
                map.merge(k1, 1, (oldValue, givenValue) -> oldValue + givenValue);
//                map.compute(k1, (k2, oldValue) -> {
//                    if (oldValue == null) {
//                        return 1;
//                    }
//                    return oldValue + 1;
//                });
//                if (map.containsKey(v)) {
//                    map.put(v, map.get(v) + 1);
//                } else {
//                    map.put(v, 1);
//                }
            }
            for (Integer times : map.values()) {
                res = Math.max(res, times);
            }
            map.clear();// 释放引用，不可达
        }
        return res;
    }
}
