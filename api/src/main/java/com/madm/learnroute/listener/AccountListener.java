package com.madm.learnroute.listener;

import com.madm.learnroute.technology.spring.AccountRegisterEvent;
import com.madm.learnroute.technology.spring.MyApplicationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * @author dongming.ma
 * @date 2023/8/21 08:39
 */
@Slf4j
@Component
public class AccountListener {

    @Async
    @TransactionalEventListener(classes = MyApplicationEvent.class)
    public void onUserRegisterEvent(MyApplicationEvent event) {
        log.info("用户保存业务完成事务触发时间为:{}", event.getSource());
    }

    @Async
    @TransactionalEventListener(classes = AccountRegisterEvent.class)
    public void onAccountRegisterEvent(AccountRegisterEvent event) {
        log.info("测试spring事务如何判断调用哪个synchronizations:{}", event.getSource());
    }

}
