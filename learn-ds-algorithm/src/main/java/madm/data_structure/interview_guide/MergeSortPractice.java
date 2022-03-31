package madm.data_structure.interview_guide;

import com.madm.learnroute.util.PrintUtils;

public class MergeSortPractice {
    public static void main(String[] args) {
//        int[] arr = {5,8,6,3,9,2,1,7};
        int[] arr = {5, 2, 3, 1, 4};
        group(arr, 0, arr.length - 1);
        PrintUtils.printArray(arr);
    }

    public static int[] group(int[] arr, int start, int end) {
        if (start < end) {
            int mid = start + ((end - start) >> 1);
            group(arr, start, mid);
            group(arr, mid + 1, end);
            merge(arr, start, mid, end);
        }
        return arr;
    }

    public static void merge(int[] arr, int start, int mid, int end) {
        int[] cond = new int[end - start + 1];
        int p1 = start;
        int p2 = mid + 1;
        int p = 0;
        while (p1 <= mid && p2 <= end) {
            cond[p++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        while (p1 <= mid) cond[p++] = arr[p1++];
        while (p2 <= end) cond[p++] = arr[p2++];
        for (int i = 0; i < cond.length; i++) {
            arr[i + start] = cond[i];
        }
    }
}
