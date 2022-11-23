package com.madm.learnroute.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dongming.ma
 * @date 2022/11/23 20:42
 */
@Getter
@AllArgsConstructor
public enum GenderEnum {
    MALE(0, "男"), FEMALE(1, "女");

    @EnumValue
    private Integer code;
    @JsonValue
    private String desc;

    @Override
    public String toString() {
        return this.desc;
    }
    public static GenderEnum getRandomGender() {
        return values()[(int) (Math.random() * 2)];
    }
}

