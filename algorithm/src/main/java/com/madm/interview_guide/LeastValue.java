package com.madm.interview_guide;

public class LeastValue {
    public static void main(String[] args) {
//        int[] ints = {8,7,6,1,2,3,4,5,6};
//        int[] ints = {7, 6, 1, 1, 3, 4, 5, 6, 7};
        int[] ints = {7, 6, 5, 3, 2, 1, 7, 8};
        long begin = System.nanoTime();
        int least = searchLeastValue(ints);
//        int least =test(ints);
        long end = System.nanoTime();
        System.out.println("最小值为：" + least + "，查找时间为：" + (end - begin));
    }

    private static int searchLeastValue(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] <= arr[mid - 1] && arr[mid] <= arr[mid + 1]) {
                return arr[mid];
            } else if (arr[mid] <= arr[mid - 1] && arr[mid] >= arr[mid + 1]) {
                left = mid + 1;
            } else if (arr[mid] >= arr[mid - 1] && arr[mid] <= arr[mid + 1]) {
                right = mid;
            }
        }
        return -1;
    }

    public static int test(int[] all) {
        //至少有3个数
        return all[smallNum(all, 0, all.length)];
//        System.out.println(all[i]);
    }

    public static int smallNum(int[] all, int left, int right) {
        if (left >= right) return left;
        int midIdx = ((left + right) / 2);
        if (midIdx == left || midIdx == right) return right;
        //斜率小于 0 递减
        if (all[midIdx + 1] - all[midIdx] < 0) {
            return smallNum(all, midIdx, right);
        } else { //斜率大于于 0 递增
            return smallNum(all, left, midIdx);
        }
    }
}
