package madm.interview_guide;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author dongming.ma
 * @date 2022/10/2 11:45
 */
public class LargeFileSort {

    private static final String BIG_FILE_NAME = "largeFile.text";
    //    private static final int LINE_COUNT = 1000000000;
    private static final int LINE_COUNT = 1000;
    private static final String SORT_FILE_NAME = "sortFile.text";
    private static final int BATCH_SIZE = 100;
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private static void createBigFile() {
        Random random = new Random();
        try (FileWriter writer = new FileWriter(BIG_FILE_NAME, UTF_8)) {
            for (int i = 0; i < LINE_COUNT; i++) {
                int val = random.nextInt(Integer.MAX_VALUE);
                writer.write(val + LINE_SEPARATOR);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> separateFile() {
        List<String> fileNameList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(BIG_FILE_NAME))) {
            int index = 0;
            List<Integer> batchLineList = new ArrayList<>(BATCH_SIZE);
            String line;
            while ((line = reader.readLine()) != null) {
                batchLineList.add(Integer.valueOf(line));
                if (batchLineList.size() == BATCH_SIZE) {
                    // 内容排序
                    batchLineList.sort(Comparator.comparingInt(a -> a));
                    // 写小文件
                    String fileName = BIG_FILE_NAME + ".tmp." + index++;
                    try (FileWriter tmpWriter = new FileWriter(fileName)) {
                        for (Integer val : batchLineList) {
                            tmpWriter.write(val + LINE_SEPARATOR);
                        }
                    }
                    fileNameList.add(fileName);
                    batchLineList.clear();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileNameList;
    }

    private static void mergeFile(List<String> fileNameList) {
//        Map<BufferedReader, String> map = new HashMap<>();
        int[] littleNumbers = new int[fileNameList.size()];
        int index = 0;
        try (FileWriter writer = new FileWriter(SORT_FILE_NAME, UTF_8)) {
            for (String fileName : fileNameList) {
                BufferedReader tmpReader = new BufferedReader(new FileReader(fileName));
                littleNumbers[index++] = Integer.valueOf(tmpReader.readLine());
                tmpReader.close();
//                map.put(tmpReader, tmpReader.readLine());
            }
            MergeSortPractice.group(littleNumbers, 0, littleNumbers.length - 1);
            for (int littleNumber : littleNumbers) {
                writer.write(littleNumber + LINE_SEPARATOR);
            }
//            while (true) {
//                boolean canRead = false;
//                Map.Entry<BufferedReader, String> minEntry = null;
//                for (Map.Entry<BufferedReader, String> entry : map.entrySet()) {
//                    String value = entry.getValue();
//                    if (value == null) {
//                        continue;
//                    }
//                    // 获取当前 reader 内容最小 entry
//                    if ((minEntry == null) || (Integer.valueOf(value) < Integer.valueOf(minEntry.getValue()))) {
//                        minEntry = entry;
//                    }
//                    canRead = true;
//                }
//                // 当且仅当所有 reader 内容为空时，跳出循环
//                if (!canRead) {
//                    break;
//                }
//                writer.write(minEntry.getValue() + LINE_SEPARATOR);
//                minEntry.setValue(minEntry.getKey().readLine());
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            // 注意关闭分片文件输入流
//            for (BufferedReader reader : map.keySet()) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public static void main(String[] args) {
        createBigFile();
        List<String> fileNameList = separateFile();
        mergeFile(fileNameList);
    }

}
