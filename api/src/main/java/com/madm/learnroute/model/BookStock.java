package com.madm.learnroute.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomUtils;

/**
 * @author dongming.ma
 * @date 2022/11/9 21:55
 */
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookStock {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "stock")
    private Long stock;

    public static BookStock create() {
        return new BookStock().setStock(RandomUtils.nextLong(0, 1000000000));
    }
}
