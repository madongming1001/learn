package com.madm.learnroute.controller;

import com.madm.learnroute.model.BookStock;
import com.madm.learnroute.service.BookStockService;
import com.mdm.model.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nullable;

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
    public RestResponse save(@RequestBody @Nullable BookStock bookStock) {
        bookStockService.save(BookStock.create());
        return RestResponse.OK();
    }
}
