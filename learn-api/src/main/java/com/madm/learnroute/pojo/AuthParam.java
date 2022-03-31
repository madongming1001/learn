package com.madm.learnroute.pojo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:54 PM
 */
@Builder
@Data
public class AuthParam {
    @NotNull(message = "学生身高不能为空")
    private String appId;
    private String appKey;
}
