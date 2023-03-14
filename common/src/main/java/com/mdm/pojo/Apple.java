package com.mdm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Apple implements Comparable<Apple> {
    private Integer id;
    private String name;
    private BigDecimal money;
    private Integer num;

    @Override
    public int compareTo(@NotNull Apple o2) {
        return this.num - o2.num;
    }
}
