package com.madm.learnroute.listener;

import com.mdm.pojo.Order;
import org.apache.lucene.queryparser.flexible.core.util.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;

/**
 * @author dongming.ma
 * @date 2022/11/14 21:32
 */
//@RocketMQTransactionListener
public class TransactionMessageListener implements RocketMQLocalTransactionListener {

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(org.springframework.messaging.Message msg, Object arg) {
        String transactionId = StringUtils.toString(msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        Order order = (Order) msg.getPayload();
        boolean result = this.saveOrder(order, transactionId);
        return result == true ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(org.springframework.messaging.Message msg) {
        String transactionId = StringUtils.toString(msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID));
        if (isSuccess(transactionId)) {
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    private boolean isSuccess(String transactionId) {
        return true;
    }

    private boolean saveOrder(Order order, String transactionId) {
        return true;
    }
}
