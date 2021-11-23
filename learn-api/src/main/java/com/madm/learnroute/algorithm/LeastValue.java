package com.madm.learnroute.algorithm;

public class LeastValue {
    public static void main(String[] args) {
        int[] arr = {8,7,6,1,2,3,4,5,6};
//        int[] arr = {7, 6, 1, 1, 3, 4, 5, 6, 7};
        System.out.println("最小值为："+searchLeastValue(arr));
    }

    private static int searchLeastValue(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int mid = left + ((right - left) >> 1);
            if (arr[mid] <= arr[mid - 1] && arr[mid] <= arr[mid + 1]) {
                return arr[mid];
            } else if (arr[mid] <= arr[mid - 1] && arr[mid] >= arr[mid + 1]) {
                left = mid  + 1;
            } else if(arr[mid] >= arr[mid - 1] && arr[mid] <= arr[mid + 1]){
                right = mid;
            }
        }
        return -1;
    }
}
