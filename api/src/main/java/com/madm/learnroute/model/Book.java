package com.madm.learnroute.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author dongming.ma
 * @date 2022/11/9 21:55
 */
@Data
@Builder
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "price")
    private BigDecimal price;

    public static Book create() {
        return new Book().price(BigDecimal.valueOf((int) Math.random() * 88 + 1));
    }
}
