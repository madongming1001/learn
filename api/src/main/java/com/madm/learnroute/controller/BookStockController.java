package com.madm.learnroute.controller;

import cn.hutool.core.util.ArrayUtil;
import com.madm.learnroute.model.BookStock;
import com.madm.learnroute.service.BookStockService;
import com.mdm.model.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author dongming.ma
 * @date 2022/11/9 23:24
 */
@RestController
@RequestMapping("/bookStock")
public class BookStockController {
    @Autowired
    BookStockService bookStockService;

    @RequestMapping("/save")
    public RestResponse save(@RequestBody BookStock... bookStock) {
        bookStockService.save(ArrayUtil.isNotEmpty(bookStock) ? bookStock[0] : BookStock.create());
        return RestResponse.OK();
    }
}
