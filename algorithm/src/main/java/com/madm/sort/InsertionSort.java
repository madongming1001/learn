package com.madm.sort;

/**
 * 插入排序 O(n2)的复杂度
 * 算法描述
 * 1、把待排序的数组分成已排序和未排序两部分，初始的时候把第一个元素认为是已排好序的。
 * 2、从第二个元素开始，在已排好序的子数组中寻找到该元素合适的位置并插入该位置。
 * 3、重复上述过程直到最后一个元素被插入有序子数组中。
 * <p>
 * 稳定性
 * 由于只需要找到不大于当前数的位置而并不需要交换，因此，直接插入排序是稳定的排序方法。
 * <p>
 * 适用场景
 * 插入排序由于O( n2 )的复杂度，在数组较大的时候不适用。但是，在数据比较少的时候，
 * 是一个不错的选择，一般做为快速排序的扩充。例如，在STL的sort算法和stdlib的qsort算法中，
 * 都将插入排序作为快速排序的补充，用于少量元素的排序。又如，在JDK 7 java.util.Arrays所用的sort方法的实现中，
 * 当待排数组长度小于47时，会使用插入排序。
 *
 * @author dongming.ma
 * @date 2023/3/14 11:45
 */
public class InsertionSort {
    public static void insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int curValue = arr[i];
            int preIndex = i - 1;
            //从右向左比较元素的同时，进行元素复制
            for (; (preIndex >= 0) && (curValue < arr[preIndex]); preIndex--)
                arr[preIndex + 1] = arr[preIndex];
            //insertValue的值插入适当位置
            arr[preIndex + 1] = curValue;
        }
    }
}
