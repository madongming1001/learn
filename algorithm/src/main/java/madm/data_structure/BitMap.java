package madm.data_structure;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author dongming.ma
 * @date 2022/6/8 23:14
 */
@Slf4j
public class BitMap {
    private char[] bytes; // 用字符数组存储元素
    private int nBits; // 指定BitMap长度

    public BitMap(int nBits) {
        this.nBits = nBits;
        this.bytes = new char[nBits / 16 + 1]; // 一个字符占2个字节，也就是2*8=16bit，最少为1 由于左移动最少需要1，所以每个index最多存储15位数
    }

    public int length() {
        return bytes.length;
    }

    /**
     * 设置int整数对应的位，修改为1
     */
    public void set(int k) {
        if (k > nBits)
            return;
        int byteIndex = k / 16;
        int bitIndex = k % 16;
        bytes[byteIndex] |= (1 << bitIndex);
    }

    /**
     * 获取int整数对应的位是否存在，true存在，false不存在
     */
    public boolean get(int k) {
        if (k > nBits)
            return false;
        int byteIndex = k / 16;
        int bitIndex = k % 16;

        return (bytes[byteIndex] & (1 << bitIndex)) != 0;
    }

    public static void main(String[] args) {
        int[] arr = new int[]{6, 2, 7, 14, 3};
        int maxArr = Arrays.stream(arr).max().getAsInt();
        // 指定BitMap长度
        BitMap bitMap = new BitMap(32);
        bitMap.set(32);
        System.out.println(bitMap.get(32));


//        // 数组的整数放进BitMap
//        for (int i = 0; i < arr.length; i++) {
//            bitMap.set(arr[i]);
//        }
//        // 判断哪些值存在
//        for (int i = 0; i < maxArr + 1; i++) {
//            log.info(i + ",是否在BitMap内-----》:" + bitMap.get(i));
//        }
    }
}