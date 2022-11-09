package madm.interview_guide;

import com.mdm.utils.RandomGeneratorNumberUtil;
import madm.sort.MergeSortPractice;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 大文件排序
 */
public class BiggestElementsSort {

    private static final Map<String, Object> cache = new ConcurrentHashMap();


    public static void main(String[] args) {
        // 1.获取数据源
        int[] sourcesData = RandomGeneratorNumberUtil.createArrayOf(1000_0000);
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
        MergeSortPractice.group(Arrays.stream(result).mapToInt(Integer::intValue).toArray());
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
        MergeSortPractice.group(Arrays.stream(rightArr).mapToInt(Integer::intValue).toArray());
        while ((leftIndex < partition) && (rightIndex < partition) && (index < partition)) {
            help[index++] = leftArr[leftIndex] < rightArr[rightIndex] ? rightArr[rightIndex++] : leftArr[leftIndex++];
        }
//         for (Integer i = 0; i < partition; i++) {
//             leftArr[i]=help[i];
//         }
        leftArr = help;
    }
}
