package madm.interview_guide;

import madm.util.RandomGeneratorNumber;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 大文件排序
 */
public class BiggestElementsSort {

    private static final Map<String,Object> cache = new ConcurrentHashMap<String,Object>();


    public static void main(String[] args) {
        // 1.获取数据源
        int[] sourcesData = RandomGeneratorNumber.createArrayOfSize(1000_0000);
        // 2.开始排序
        long begin = System.currentTimeMillis();
        Integer[] processor = BiggestElementsSort.processor(sourcesData, 2);
        long end = System.currentTimeMillis();
        System.out.println(end - begin);
        List<Integer> collect = Arrays.stream(processor).collect(Collectors.toList());
        System.out.println(collect);
    }

    private static Integer[] processor(int[] sourcesData, int partition) {
        int index = partition;
        Integer[] nextArr = new Integer[partition];

        // 1.结果集初始化
        Integer[] result = new Integer[partition];
        for (Integer i = 0; i < partition; i++) {
            result[i] = sourcesData[i];
        }
        grouping(result, 0, partition - 1);

        // 2.分段比较开始
        while (index < sourcesData.length) {
            for (Integer i = 0; i < partition; i++) {
                nextArr[i] = sourcesData[index++];
            }
            merge(result, nextArr, partition);
        }
        return result;
    }

    public static void merge(Integer[] leftArr, Integer[] rightArr, Integer partition) {
        System.out.println(leftArr);
        // 1.中转数组
        Integer[] help = new Integer[partition];
        int leftIndex = 0;
        int rightIndex = 0;
        int index = 0;
        grouping(rightArr, 0, partition - 1);
        while ((leftIndex < partition) && (rightIndex < partition) && (index < partition)) {
            help[index++] = leftArr[leftIndex] < rightArr[rightIndex] ? rightArr[rightIndex++] : leftArr[leftIndex++];
        }
//         for (Integer i = 0; i < partition; i++) {
//             leftArr[i]=help[i];
//         }
        leftArr = help;
    }

    public static void grouping(Integer[] arr, int L, int R) {
        if (L >= R) {
            return;
        }
        int mid = L + ((R - L) >> 1);
        grouping(arr, L, mid);
        grouping(arr, mid + 1, R);
        merge(arr, L, mid, R);
    }

    public static void merge(Integer[] arr, int L, int Mid, int R) {
        int[] help = new int[R - L + 1];
        int i = 0;
        int left = L;
        int right = Mid + 1;
        while (left <= Mid && right <= R) {
            help[i++] = arr[right] >= arr[left] ? arr[right++] : arr[left++];
        }

        while (left <= Mid) {
            help[i++] = arr[left++];
        }
        while (right <= R) {
            help[i++] = arr[right++];
        }

        for (i = 0; i < help.length; i++) {
            arr[L + i] = help[i];
        }
    }
}
