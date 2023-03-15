package com.madm.learnroute;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author dongming.ma
 * @date 2022/11/29 15:55
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class LearnApiApplicationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void contextLoads(){

    }
}