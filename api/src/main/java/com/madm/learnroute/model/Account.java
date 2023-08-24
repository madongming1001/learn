package com.madm.learnroute.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.madm.learnroute.enums.GenderEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;

import static com.madm.learnroute.enums.GenderEnum.getRandomGender;

/**
 * @author dongming.ma
 * @date 2022/11/9 21:55
 */
@Data
@Accessors(chain = true)
@TableName("account")
public class Account {
    @TableId(type = IdType.AUTO)
    private Long id;
    private BigDecimal balance;
    @TableField(value = "username")
    private String userName;
    @ApiModelProperty(value = "性别")
    private GenderEnum gender;

    public static Account createAccount() {
        return new Account().setBalance(BigDecimal.valueOf((int) Math.random() * 88 + 1)).
                                setUserName(RandomUtils.nextInt(0, 1000000000) + StringPool.UNDERSCORE + "auto").
                                setGender(getRandomGender());
    }

}
