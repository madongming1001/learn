package com.madm.learnroute.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:54 PM
 */
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthParam {
    @Pattern(regexp = "(^[0-9][-UA-])([0-9],)")
    @NotNull(message = "学生身高不能为空")
    private String appId;
    private String appKey;
}
