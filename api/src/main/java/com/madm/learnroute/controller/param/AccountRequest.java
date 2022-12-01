package com.madm.learnroute.controller.param;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @author dongming.ma
 * @date 2022/11/28 21:07
 */
@Data
public class AccountRequest {
    private int id;
    //    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ctime;
    //    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate utime;
}