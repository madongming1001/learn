package com.madm.learnroute.controller;

import cn.hutool.core.util.ArrayUtil;
import com.madm.learnroute.model.Book;
import com.madm.learnroute.service.impl.BookServiceImpl;
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
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookServiceImpl bookService;

    @RequestMapping("/save")
    public RestResponse save(@RequestBody @Nullable Book... book) {
        bookService.save(ArrayUtil.isNotEmpty(book) ? book[0] : Book.create());
        return RestResponse.OK();
    }
}