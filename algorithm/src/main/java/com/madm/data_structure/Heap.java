package com.madm.data_structure;

import java.util.Arrays;
import java.util.Stack;

/**
 * 大顶堆 数组话显示
 * {0, 33, 17, 31, 16, 13, 15, 9, 5, 6, 7, 8, 1, 2, 0}
 * 堆排序不是稳定的排序算法，因为在排序的过程，存在将堆的最后一个节点跟堆顶节点互换的操作，所以就有可能改变值相同数据的原始相对顺序。
 * 非稳定
 * 如果节点的下标是 i，那左子节点的下标就是 2*i+1，右子节点的下标就是 2*i+2，父节点的下标就是 i−1/2。
 * 利用数组存储堆
 * 
 */
public class Heap {
    private int[] a; // 数组，从下标1开始存储数据
    private int n;  // 堆可以存储的最大数据个数
    private int count; // 堆中已经存储的数据个数

    public Heap(int capacity) {
        a = new int[capacity + 1];
        n = capacity;
        count = 0;
    }

    public Heap(int[] arr) {
        a = arr;
        n = arr.length + 1;
        count = arr.length - 2;
    }

    public Heap() {

    }

    public void insert(int data) {
        if (count >= n) return; // 堆满了
        ++count;
        a[count] = data;
        int i = count;
        while (i / 2 > 0 && a[i] > a[i / 2]) { // 自下往上堆化
            swap(a, i, i / 2); // swap()函数作用：交换下标为i和i/2的两个元素
            i = i / 2;
        }
    }


    /**
     * 最大堆的删除  从上往下的堆化
     */
    public void removeMax() {
        if (count == 0) return; // 堆中没有数据
        a[1] = a[count];
        --count;
        heapify(a, count, 1);
    }

    /**
     * 第二种方式建立堆
     *
     * 从后往前处理数组，并且每个数据都是从上往下堆化。
     *
     * 因为叶子节点往下堆化只能自己跟自己比较，所以我们直接从最后一个非叶子节点开始，依次堆化就行了。
     */
    private void buildHeap(int[] a, int n) {
        //i= n/2 可以拿到最后一个非叶子节点
        for (int i = n / 2; i >= 1; --i) {
            heapify(a, n, i);
        }
    }

    private void heapify(int[] a, int n, int i) { // 自上往下堆化
        while (true) {
            int maxPos = i;
            if (i * 2 <= n && a[i] < a[i * 2]) maxPos = i * 2;
            if (i * 2 + 1 <= n && a[maxPos] < a[i * 2 + 1]) maxPos = i * 2 + 1;
            //当头节点大于左节点和右节点的时候
            if (maxPos == i) break;
            swap(a, i, maxPos);
            i = maxPos;
        }
    }

    private void swap(int[] a, int p1, int p2) {
        int temp = a[p1];
        a[p1] = a[p2];
        a[p2] = temp;
    }

    //n表示数据的个数，数组a中的数据从下标1到n的位置。
    //包括建堆和排序
    //建堆过程的时间复杂度是 O(n)
    //排序过程的时间复杂度是 O(nlogn)
    //堆排序整体的时间复杂度是 O(nlogn)
    public void sort(int[] a, int n) {
        buildHeap(a, n);//构建数据结构为O(n)
        int k = n;
        while (k > 1) {
            swap(a, 1, k);
            --k;
            heapify(a, k, 1);
        }
    }

    /**
     * 优先级队列实现小顶堆
     */
    private static <T> void siftUpComparable(int k, T x, Object[] arr) {
        Comparable<? super T> key = (Comparable<? super T>) x;
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = arr[parent];
            if (key.compareTo((T) e) >= 0)
                break;
            arr[k] = e;
            k = parent;
        }
        arr[k] = key;
    }

    public static void main(String[] args) {
        Heap heap = new Heap();
        int[] intr = {0, 33, 17, 31, 16, 13, 15, 9, 5, 6, 7, 8, 1, 2, 0};
        heap.sort(intr,intr.length);
        System.out.println(Arrays.toString(intr));
    }
}
