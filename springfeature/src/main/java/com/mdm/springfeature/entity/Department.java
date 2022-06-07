package com.mdm.springfeature.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: madongming
 * @DATE: 2022/6/7 18:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    private Long id;
    private Integer depno;
    private String depname;
    private String memo;
}
