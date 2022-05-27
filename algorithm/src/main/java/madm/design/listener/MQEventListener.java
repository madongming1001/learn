package madm.design.listener;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author MaDongMing
 * @Date 2022/4/1 6:47 PM
 */
@Slf4j
public class MQEventListener implements EventListener {
    @Override
    public void doEvent(LotteryResult result) {
        log.info("记录用户{}摇号结果(MQ)：{}", result.getUid(),
                result.getMsg());
    }
}