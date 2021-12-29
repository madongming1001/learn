package madm.data_structure.design.build_pattern;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class DecorationPackageController {
    public String getMatterList(BigDecimal area, Integer level) {
        List<Matter> list = new ArrayList<Matter>();
        BigDecimal price = BigDecimal.ZERO;
        if (1 == level) {
            LevelTwoCeiling levelTwoCeiling = new LevelTwoCeiling();
            DuluxCoat duluxCoat = new DuluxCoat();

            ShengXiangFloor shengXiangFloor = new ShengXiangFloor();
            list.add(levelTwoCeiling);
            list.add(duluxCoat);
            list.add(shengXiangFloor);
            price = price.add(area.multiply(new
                    BigDecimal("0.2")).multiply(levelTwoCeiling.price()));
            price = price.add(area.multiply(new
                    BigDecimal("1.4")).multiply(duluxCoat.price()));
            price = price.add(area.multiply(shengXiangFloor.price()));
        }
        if (2 == level) {
            LevelTwoCeiling levelTwoCeiling = new LevelTwoCeiling();
            LiBangCoat liBangCoat = new LiBangCoat();
            MarcoPoloTile marcoPoloTile = new MarcoPoloTile();
            list.add(levelTwoCeiling);
            list.add(liBangCoat);
            list.add(marcoPoloTile);
            price = price.add(area.multiply(new
                    BigDecimal("0.2")).multiply(levelTwoCeiling.price()));
            price = price.add(area.multiply(new
                    BigDecimal("1.4")).multiply(liBangCoat.price()));
            price = price.add(area.multiply(marcoPoloTile.price()));
        }
        if (3 == level) {
            LevelOneCeiling levelOneCeiling = new LevelOneCeiling();
            LiBangCoat liBangCoat = new LiBangCoat();
            DongPengTile dongPengTile = new DongPengTile();
            list.add(levelOneCeiling);
            list.add(liBangCoat);
            list.add(dongPengTile);
            price = price.add(area.multiply(new
                    BigDecimal("0.2")).multiply(levelOneCeiling.price()));
            price = price.add(area.multiply(new
                    BigDecimal("1.4")).multiply(liBangCoat.price()));
            price = price.add(area.multiply(dongPengTile.price()));
        }
        StringBuilder detail = new StringBuilder(level + String.valueOf(price.setScale(2, RoundingMode.HALF_UP)) + area.doubleValue());
        for (Matter matter : list) {
            detail.append(matter.scene()).append("ғ").append(matter.brand()).append("̵").append(matter.model()).append(matter.price());
        }
        return detail.toString();
    }
}
