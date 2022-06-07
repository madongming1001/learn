package com.mdm.springfeature.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * @Author: madongming
 * @DATE: 2022/6/7 18:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Long id;
    private Integer empno;
    private String empname;
    private String job;
    private Integer mgr;
    private Timestamp hiredate;
    private BigDecimal sal;
    private BigDecimal comn;
    private Integer depno;
}
