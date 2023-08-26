package com.madm.sort;

import java.util.Arrays;

/**
 * 归并排序 O（nlogn）
 * <p>
 * 算法描述
 * 两种方法
 * <p>
 * 递归法（Top-down）
 * 1、申请空间，使其大小为两个已经排序序列之和，该空间用来存放合并后的序列
 * 2、设定两个指针，最初位置分别为两个已经排序序列的起始位置
 * 3、比较两个指针所指向的元素，选择相对小的元素放入到合并空间，并移动指针到下一位置
 * 4、重复步骤3直到某一指针到达序列尾
 * 5、将另一序列剩下的所有元素直接复制到合并序列尾
 * <p>
 * 迭代法（Bottom-up）
 * 原理如下（假设序列共有n个元素）：
 * <p>
 * 1、将序列每相邻两个数字进行归并操作，形成ceil(n/2)个序列，排序后每个序列包含两/一个元素
 * 2、若此时序列数不是1个则将上述序列再次归并，形成ceil(n/4)个序列，每个序列包含四/三个元素
 * 3、重复步骤2，直到所有元素排序完毕，即序列数为1
 * <p>
 * 稳定性
 * 因为我们在遇到相等的数据的时候必然是按顺序“抄写”到辅助数组上的，所以，归并排序同样是稳定算法。
 */
public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {5, 8, 6, 3, 9, 2, 1, 7};
//        int[] arr = {5, 2, 3, 1, 4};
        group(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static void merge2(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        int step = 1;
        int N = arr.length;
        while (step < N) {
            int L = 0;
            while (L < N) {
                int M;
                if (N - L >= step) {
                    M = L + step - 1;
                } else {
                    M = N - 1;
                }
                if (M == N - 1) {
                    break;
                }
                int R;
                if (N - 1 - M >= step) {
                    R = M + step;
                } else {
                    R = N - 1;
                }
                merge(arr, L, M, R);
                if (R == N - 1) {
                    break;
                } else {
                    L = R + 1;
                }
                if (step > N / 2) {
                    break;
                }
                step *= 2;
            }
        }
    }


    public static int[] group(int[] arr) {
        return group(arr, 0, arr.length - 1);
    }

    private static int[] group(int[] arr, int start, int end) {
        if (start < end) {
            //middle
            //int mid = (start + end) / 2;
            int mid = start + ((end - start) >> 1);
            group(arr, start, mid);
            group(arr, mid + 1, end);
            merge(arr, start, mid, end);
        }
        return arr;
    }

    private static void merge(int[] arr, int start, int mid, int end) {
        int[] help = new int[end - start + 1];//传的是下标位置所以需要加1
        int p = 0;
        int p1 = start;
        int p2 = mid + 1;
        while (p1 <= mid && p2 <= end) {
            help[p++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid) help[p++] = arr[p1++];
        while (p2 <= end) help[p++] = arr[p2++];
        //把排完序的数值写回原数组
        for (int i = 0; i < help.length; i++) {
            arr[i + start] = help[i];
        }
    }
}
