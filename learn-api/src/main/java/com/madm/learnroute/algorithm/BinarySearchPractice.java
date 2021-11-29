package com.madm.learnroute.algorithm;

public class BinarySearchPractice {

    public static void main(String[] args) {
//        int[] arr = {7,6,5,3,1,4,5,6,7};
//        int target = 1;
//        System.out.println(binarySearch(arr,target));
//        System.out.println(binarySearch1(arr,target));
//        System.out.println(binarrySort(arr,target));
        int left = 1500_0000_00;
        int right = 1500_0000_00;
        int mid = (right + left) + 2;
        System.out.println(mid);
    }

    private static int binarrySort(int[] arr,int target){
        int left = 0,right = arr.length -1;
        while(left < right){
            int mid = left + ((right - left) / 2);
//            int mid = (right + left) + 2;
            if(arr[mid] < target){
                left = mid + 1;
            }else{
                right = mid;
            }
        }
        return arr[left] == target ? left : -1;
    }

    private static int binarySearch(int[] nums,int target) {
        int left = 0,right = nums.length-1;
        while(left < right){
            int mid = left + ((right - left) / 2);
            if(nums[mid] < target){
                left = mid + 1;
            }else{
                right = mid;
            }
        }
        return nums[left] == target ? left : -1;
    }
    private static int binarySearch1(int[] nums,int target) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return nums[left] == target ? left : -1;
    }


}
