package madm.data_structure.design.build_pattern;

import java.math.BigDecimal;

public class MarcoPoloTile implements Matter{
    @Override
    public String scene() {
        return "地砖";
    }

    @Override
    public String brand() {
        return "马可波罗（MARCO POLO）";
    }

    @Override
    public String model() {
        return "缺省";
    }

    @Override
    public BigDecimal price() {
        return new BigDecimal(140);
    }

    @Override
    public String desc() {
        return "马可波罗";
    }
}
