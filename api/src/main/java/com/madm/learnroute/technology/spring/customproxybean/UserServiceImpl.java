package com.madm.learnroute.technology.spring.customproxybean;

import org.springframework.stereotype.Service;

/**
 * @author dongming.ma
 * @date 2022/6/30 11:39
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public void userInfo() {
        System.out.println("打印了用户信息 UserServiceImpl1");
    }
}