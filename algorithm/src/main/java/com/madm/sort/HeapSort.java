package com.madm.sort;

import java.util.Arrays;

import static cn.hutool.core.util.ArrayUtil.swap;

/**
 * 堆排序
 * 1、先让整个数组都变成大根堆结构，变成堆的过程：
 * 1）从上到下的方法，时间复杂度为O（N*logN）
 * 2）从下到上的方法，时间复杂度为O（N）
 * 2、把堆的最大值和堆末尾的值交换，然后减少堆的大小之后，再去调整堆，一直周而复始，时间复杂度为O（N*logN）
 * 3、堆的大小减少成0之后，排序完成
 */
public class HeapSort {

    public static void main(String[] args) {
        int[] arr = {7, 5, 15, 3, 17, 2, 20, 24, 1, 9, 12, 8};
        System.out.println(findLargestNumberThanK(arr, 5));
        heapSort(arr);
        System.out.println(Arrays.toString(arr));
    }

    public static int findLargestNumberThanK(int[] arr, int k) {
        // 用前k个元素构建最小堆
        buildHeap(arr, k);
        // 继续遍历数组，和堆顶比较
        for (int i = k; i < arr.length; i++) {
            if (arr[i] > arr[0]) {
                arr[0] = arr[i];
                downAdjust(arr, 0, k);
            }
        }
        return arr[0];
    }

    private static void buildHeap(int[] arr, int index) {
        // 从最后一个非叶子节点开始，依次下沉调整
        for (int i = (index - 2) / 2; i >= 0; i--) {
            downAdjust(arr, i, index);
            System.out.println("第" + i + "次输出");
        }
    }

    private static void downAdjust(int[] arr, int index, int length) {
        //temp保存父节点的值，用于最后的赋值
        int temp = arr[index];
        int childIndex = (2 * index) + 1;
        while (childIndex < length) {
            // 如果有右孩子，且右孩子小于左孩子的值，则定位到右孩子
            if (((childIndex + 1) < length) && (arr[childIndex + 1] < arr[childIndex])) {
                childIndex++;
            }
            // 如果父节点的值小于任何一个孩子的值，直接跳出
            if (temp <= arr[childIndex]) {
                break;
            }
            // 无需真正的交换，单向赋值即可
            arr[index] = arr[childIndex];
            index = childIndex;
            childIndex = (2 * childIndex) + 1;
        }
        arr[index] = temp;
    }

    private static void initializeHeap(int[] arr, int index) {
        //新进来的数，现在停在了index位置，请依次往上移动，
        //移动到0位置，或者干不掉自己的父亲，停！
        while (arr[index] > arr[(index - 1) / 2]) {
            swap(arr, index, (index - 1) / 2);
            index = (index - 1) / 2;
        }
    }

    //从index的位置，往下看，不断的下沉
    //停：较大的孩子都不再比index位置的数大；已经没孩子了
    private static void heapify(int[] arr, int index, int heapSize) {
        int left = index * 2 + 1, right;
        while (left < heapSize) {//如果有左孩子，有没有右孩子，可能有可能没有！
            int largest = (right = left + 1) < heapSize && arr[right] > arr[left] ? right : left;//把较大孩子的下标，给largest
            largest = arr[largest] > arr[index] ? largest : index;
            if (largest == index) {
                break;
            }
            swap(arr, largest, index);//index和较大孩子，要互换
            index = largest;
            left = index * 2 + 1;
        }
    }

    //堆排序额外空间复杂度O(1)
    public static void heapSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        //O(N*logN)
        for (int i = 0; i < arr.length; i++) {// O(N)
            initializeHeap(arr, i);//O(logN)
        }
        //O(N)
        for (int i = arr.length - 1; i >= 0; i--) {
            heapify(arr, i, arr.length);
        }
        int heapSize = arr.length;
        swap(arr, 0, --heapSize);
        //O(N*logN)
        while (heapSize > 0) {//O(N)
            heapify(arr, 0, heapSize);//O(logN)
            swap(arr, 0, --heapSize);//O(1)
        }
    }
}
