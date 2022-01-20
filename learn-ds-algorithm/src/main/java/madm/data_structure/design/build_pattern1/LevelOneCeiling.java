package madm.data_structure.design.build_pattern1;

import java.math.BigDecimal;

public class LevelOneCeiling implements Matter {
    public String scene() {
        return "吊顶";
    }
    public String brand() {
        return "装修公司自带";
    }
    public String model() {
        return "一级顶";
    }
    public BigDecimal price() {
        return new BigDecimal(260);
    }
    public String desc() {
        return "造假制作低一级，只有一个层次的吊顶，一般离顶120-150mm";
    }
}
