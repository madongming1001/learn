package com.madm.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

/**
 * 桶排序 时间复杂度O(n) 空间复杂度O(n)
 * 桶排序的性能并非绝对稳定，如果元素的分布极不均衡，在极端情况下，
 * 第一个桶中有n-1个元素，最后一个桶中有1个元素。此时的时间复杂度将退化为O(nlogn)，
 * 而且还白白创建了许多空桶。
 *
 * @author dongming.ma
 * @date 2023/3/14 15:30
 */
public class BucketSort {
    public static void main(String[] args) {
        double[] arr = {4.12, 6.421, 0.0023, 3.0, 2.123, 8.122, 4.12, 10.09};
        double[] sortedArr = bucketSort(arr);
        System.out.println(Arrays.toString(sortedArr));
    }

    public static double[] bucketSort(double[] arr) {
        //1.得到数列的最大值和最小值，并算出差值d
        double max = arr[0];
        double min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            max = Math.max(arr[i], max);
            min = Math.min(arr[i], min);
        }

        double d = max - min;
        //2.初始化桶
        int bucketNum = arr.length;
        ArrayList<LinkedList<Double>> bucketList = new ArrayList<>(bucketNum);
        for (int i = 0; i < bucketNum; i++) {
            bucketList.add(new LinkedList());
        }

        //3.遍历原始数组，将每个元素放入桶中
        for (int i = 0; i < arr.length; i++) {
            int num = (int) ((arr[i] - min) * (bucketNum - 1) / d);
            bucketList.get(num).add(arr[i]);
        }

        //4。对每个桶内部进行排序
        for (int i = 0; i < bucketList.size(); i++) {
            //JDK底层采用了归并排序或归并的优化版本
            Collections.sort(bucketList.get(i));//O(nlogn)
        }

        //5.输出全部元素
        double[] sortedArr = new double[arr.length];
        int index = 0;
        for (LinkedList<Double> list : bucketList) {
            for (Double element : list) {
                sortedArr[index] = element;
                index++;
            }
        }
        return sortedArr;
    }
}
