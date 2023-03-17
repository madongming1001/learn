package com.madm.design.strategy.classmethod;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class QueryGrantTypeService {

    @Autowired
    private GrantTypeSerive grantTypeSerive;

    private Map<String, Function<String, String>> grantTypeMap = new HashMap<>();

    /**
     * 初始化业务分派逻辑,代替了if-else部分
     * key: 优惠券类型
     * value: lambda表达式,最终会获得该优惠券的发放方式
     */
    @PostConstruct
    public void dispatcherInit() {
        grantTypeMap.put("红包", resourceId -> grantTypeSerive.redPaper(resourceId));
        grantTypeMap.put("购物券", resourceId -> grantTypeSerive.shopping(resourceId));
        grantTypeMap.put("qq会员", resourceId -> grantTypeSerive.QQVip(resourceId));
    }

    public String getResult(String resourceType, String resourceId) {
        //Controller根据 优惠券类型resourceType、编码resourceId 去查询 发放方式grantType
        Function<String, String> result = grantTypeMap.get(resourceType);
        if (result != null) {
            //传入resourceId 执行这段表达式获得String型的grantType
            return result.apply(resourceId);
        }
        return "查询不到该优惠券的发放方式";
    }
}

