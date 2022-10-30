package com.mdm.utils;

import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * @author dongming.ma
 * @date 2022/10/21 22:26
 */
@NoArgsConstructor
public class GenerateDataUtil {

    private String filePath = StringUtils.EMPTY;

    private static Random random = new Random();

    public GenerateDataUtil(String filePath) {
        this.filePath = filePath;
    }

    public static int generateRandomData(int start, int end) {
        return random.nextInt(end - start + 1) + start;
    }

    /**
     * 产生10G的 1-1000的数据
     */
    public void generateData() throws IOException {
        File file = new File("User.dat");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        int start = 18;
        int end = 70;
        long startTime = System.currentTimeMillis();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            //Integer.MAX_VALUE * 1.7 = 365_9722_1999 不可数循环增加safepoint 轮训标志 方式是改变内存的类型 可读不可读 然后触发中断响应事件
            for (long i = 1; i < Integer.MAX_VALUE * 1.7; i++) {
                String data = StringUtils.join(generateRandomData(start, end), ",");
                bw.write(data);
                // java1.7特性 可以在数字之间添加下划线 增加可读性 try-with-resources
                // 每100万条记录成一行，100万条数据大概4M
                if (i % 100_0000 == 0) {
                    bw.write("\n");
                }
            }
            System.out.println("写入完成! 共花费时间:" + (System.currentTimeMillis() - startTime) / 1000 + " s");
        }
    }

//    public static void main(String[] args) {
//        GenerateDataUtil generateData = new GenerateDataUtil();
//        try {
//            generateData.generateData();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
