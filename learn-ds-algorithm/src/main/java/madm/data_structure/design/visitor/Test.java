package madm.data_structure.design.visitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    public static void main(String[] args) {
        DataView dataView = new DataView();
        log.info("\r\n家长视角访问：");
        // 家长
        dataView.show(new Parent());
        log.info("\r\n校长视角访问：");
        // 校长
        dataView.show(new Principal());

    }
}
