package madm.data_structure.design.visitor;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
public class Test {
    public static void main(String[] args) {
//        DataView dataView = new DataView();
//        log.info("\r\n家长视角访问：");
//        // 家长
//        dataView.show(new Parent());
//        log.info("\r\n校长视角访问：");
//        // 校长
//        dataView.show(new Principal());
//        System.out.println(Integer.parseInt("65534",2));
        System.out.println(Integer.toBinaryString(65535));
        System.out.println(Integer.toBinaryString(4096));
        String systemDefaultEncoding = System.getProperty("file.encoding");
        //获取当前系统的默认字符集
        System.out.println("当前系统的默认字符集：" + systemDefaultEncoding);

        //测试文本
//        String str = "我是测试文本";
        String str = "[{\"avatarUrl\":\"1\",\"nickName\":\"1\",\"userId\":\"1\"},{\"avatarUrl\":\"2\",\"nickName\":\"2\",\"userId\":\"2\"}]";

        //使用默认字符集，对字符串和字符数组进行相互转化
        byte[] bytes = new byte[0];
        try {
            bytes = str.getBytes("utf-8");
            System.out.println("使用" + systemDefaultEncoding + "字符集，测试中文占字节数是：" + bytes.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String result = new String(bytes);

        System.out.println("转化后的结果为：" + result);

        System.out.println(toUTF8("\\u5B57\\u8282\\u957F\\u5EA6\\u9700\\u8981\\u5728{min}\\u5230{max}\\u4E4B\\u95F4"));
    }

    /**
     * 将ISO-8859-1编码字符串转换UTF-8编码
     *
     * @param param
     * @return
     */
    public static String toUTF8(String param) {
        byte[] bytes = param.getBytes(StandardCharsets.ISO_8859_1);
        System.out.println(bytes);
        if (param == null) {
            return null;
        } else {
            try {
                param = new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return param;
            }
        }
        return param;
    }
}
