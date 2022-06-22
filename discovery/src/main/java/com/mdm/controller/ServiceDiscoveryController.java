package com.mdm.controller;

import com.mdm.model.Response;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dongming.ma
 * @date 2022/5/28 16:01
 */
@RestController
@RequestMapping("discovery")
public class ServiceDiscoveryController {

    @RequestMapping("/success")
    public Response ok() {
        return Response.success();
    }

}
