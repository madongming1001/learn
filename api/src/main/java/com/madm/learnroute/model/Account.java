package com.madm.learnroute.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomUtils;

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
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField(value = "balance")
    private BigDecimal balance;
    @TableField(value = "username")
    private String userName;

    public static Account create() {
        return new Account().balance(BigDecimal.valueOf((int) Math.random() * 88 + 1)).userName(RandomUtils.nextInt(0, 1000000000) + StringPool.UNDERSCORE + "auto");
    }
}
