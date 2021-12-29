package madm.data_structure.design.build_pattern;

import java.math.BigDecimal;

public class DerFloor implements Matter{
    @Override
    public String scene() {
        return "地板";
    }

    @Override
    public String brand() {
        return "德尔（Der）";
    }

    @Override
    public String model() {
        return "A+";
    }

    @Override
    public BigDecimal price() {
        return new BigDecimal(119);
    }

    @Override
    public String desc() {
        return "DER德尔";
    }
}
