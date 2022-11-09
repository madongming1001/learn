package madm.interview_guide;

/**
 * 我们要把相邻的元素两两比较，当一个元素大于右侧相邻元素时，交换交换它们的位置：当一个元素小于或等于右侧相邻元素时，位置不变
 *
 * @author dongming.ma
 * @date 2022/11/9 20:13
 */
public class BubbleSortPractice {

    public static void main(String[] args) {
        bubbleSort1();
    }

    private static void bubbleSort1() {
        int[] arr = {6, 3, 8, 2, 9, 1};
        System.out.println("排序前数组为：");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        for (int i = 0; i < arr.length - 1; i++) {//表示n次排序过程。
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {//前面的数字大于后面的数字就交换
                    //交换a[j-1]和a[j]
                    swap(arr, j);
                }
            }
        }
        System.out.println();
        System.out.println("排序后的数组为：");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    private static void bubbleSort2() {
        int[] arr = {6, 3, 8, 2, 9, 1};
        System.out.println("排序前数组为：");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        for (int i = 0; i < arr.length - 1; i++) {//表示n次排序过程。
            boolean isSorted = true;
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {//前面的数字大于后面的数字就交换
                    //交换a[j-1]和a[j]
                    swap(arr, j);
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
        }
        System.out.println();
        System.out.println("排序后的数组为：");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    private static void bubbleSort3() {
        int[] arr = {6, 3, 8, 2, 9, 1};
        System.out.println("排序前数组为：");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        int lastExchangeIndex = 0;
        int sortBorder = arr.length - 1;
        for (int i = 0; i < arr.length - 1; i++) {//表示n次排序过程。
            boolean isSorted = true;
            for (int j = 0; j < sortBorder; j++) {
                if (arr[j] > arr[j + 1]) {//前面的数字大于后面的数字就交换
                    //交换a[j-1]和a[j]
                    swap(arr, j);
                    isSorted = false;
                    lastExchangeIndex = j;
                }
            }
            sortBorder = lastExchangeIndex;
            if (isSorted) {
                break;
            }
        }
        System.out.println();
        System.out.println("排序后的数组为：");
        for (int num : arr) {
            System.out.print(num + " ");
        }
    }

    private static void swap(int[] arr, int j) {
        int temp = arr[j + 1];
        arr[j + 1] = arr[j];
        arr[j] = temp;
    }
}