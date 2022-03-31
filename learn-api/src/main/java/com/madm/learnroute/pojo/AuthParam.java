package com.madm.learnroute.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author MaDongMing
 * @Date 2022/3/30 6:54 PM
 */
@Data
public class AuthParam {
    private String appId;
    private String appKey;
}
