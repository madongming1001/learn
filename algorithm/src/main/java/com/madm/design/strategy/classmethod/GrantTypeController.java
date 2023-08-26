package com.madm.design.strategy.classmethod;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

public class GrantTypeController {
    @Autowired
    private QueryGrantTypeService queryGrantTypeService;

    @PostMapping("/grantType")
    public String test(String resourceName, String resourceId) {
        return queryGrantTypeService.getResult(resourceName, resourceId);
    }
}
