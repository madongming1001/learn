package com.madm.interview_guide;

public class BinarySearchPractice {

    public static void main(String[] args) {
//        int[] arr = {7,6,5,3,1,4,5,6,7};
//        int target = 1;
//        System.out.println(binarySearch(arr,target));
//        System.out.println(binarySearch1(arr,target));
//        System.out.println(binarrySort(arr,target));
//        int left = 1500_0000_00;
//        int right = 1500_0000_00;
//        int mid = (right + left) + 2;
//        System.out.println(mid);
        int[] arr = {3, 5, 6, 8, 9, 10};
        System.out.println(binarrySort(arr, 10));
//        System.out.println(binarySearchLastLessForValue(arr, arr.length, 7));
    }

    private static int binarrySort(int[] arr, int target) {
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + ((right - left) >> 1);
//            int mid = (right + left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] > target) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 查找最后一个小于等于给定值的元素
     */
    public static int binarySearchLastLessForValue(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else {
                if ((mid == n - 1) || (a[mid + 1] > value)) return mid;
                else low = mid + 1;
            }
        }
        return -1;
    }

    public static int binarySearch(int[] ints, int target) {
        int left = 0;
        int right = ints.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (ints[mid] == target) {
                return mid;
            } else if (ints[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return -1;
    }

    public int bsearch(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] >= value) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (low < n && a[low] == value) return low;
        return -1;
    }

    /**
     * 查找第一个值等于给定值的元素
     */
    public int binarySearchFirstForValue(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                //等于的情况
                if ((mid == 0) || (a[mid - 1] != value)) return mid;
                else high = mid - 1;
            }
        }
        return -1;
    }

    /**
     * 查找最后一个值等于给定值的元素
     */
    public int binarySearchLastForValue(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                if ((mid == n - 1) || (a[mid + 1] != value)) return mid;
                else low = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 查找第一个大于等于给定值的元素
     */
    public int binarySearchFirstGreaterForValue(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] >= value) {
                if ((mid == 0) || (a[mid - 1] < value)) return mid;
                else high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    //arr中，有两种数，出现奇数次
    public static void printOldTimesNums2(int[] arr) {
        int eor = 0;
        for (int i = 0; i < arr.length; i++) {
            eor ^= arr[i];
        }
        // eor = a ^ b
        // eor != 0
        // eor 必然有一个位置上是1
        //0110010000
        //0000010000
        int rightOne = eor & (~eor + 1);//提取出最右侧的1
        int onlyOne = 0;//eor‘
        for (int i = 0; i < arr.length; i++) {
            if ((arr[i] & rightOne) != 0) {
                onlyOne ^= arr[i];
            }
        }
        System.out.println(onlyOne + "、" + (eor ^ onlyOne));
    }


}
