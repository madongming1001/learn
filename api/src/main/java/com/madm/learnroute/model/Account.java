package com.madm.learnroute.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author dongming.ma
 * @date 2022/11/9 21:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStock1 {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "balance")
    private BigDecimal balance;
    @TableField(value = "username")
    private String userName;
}
