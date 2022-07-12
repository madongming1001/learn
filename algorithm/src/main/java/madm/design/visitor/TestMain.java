package madm.design.visitor;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TestMain {
    public static void main(String[] args) {
        DataView dataView = new DataView();
        log.info(System.lineSeparator() + "家长视角访问：");
        // 家长
        dataView.show(new Parent());
        log.info(System.lineSeparator() + "校长视角访问：");
        // 校长
        dataView.show(new Principal());
    }
}