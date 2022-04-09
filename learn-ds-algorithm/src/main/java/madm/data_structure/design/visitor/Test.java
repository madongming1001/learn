package madm.data_structure.design.visitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test {
    public static void main(String[] args) {
        DataView dataView = new DataView();
        log.info("\r\n家长视角访问：");
        dataView.show(new Parent()); // 家长
        log.info("\r\n校长视角访问：");
        dataView.show(new Principal()); // 校长
    }
}
