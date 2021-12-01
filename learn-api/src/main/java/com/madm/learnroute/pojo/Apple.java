package com.madm.learnroute.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Apple {
    private Integer id;
    private String name;
    private BigDecimal money;
    private Integer num;
}
