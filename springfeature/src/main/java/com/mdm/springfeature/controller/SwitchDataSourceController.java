package com.mdm.springfeature.controller;

import com.mdm.springfeature.service.SwitchDataSourceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: madongming
 * @DATE: 2022/6/7 19:12
 */
@RestController
public class SwitchDataSourceController {

    @Resource
    SwitchDataSourceService switchDataSourceService;


    @RequestMapping("/show")
    public Object showAfterSwitchData() {
        return switchDataSourceService.showAfterSwitchData();
    }


}
