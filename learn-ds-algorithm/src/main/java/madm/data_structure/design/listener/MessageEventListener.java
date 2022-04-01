package madm.data_structure.design.listener;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author MaDongMing
 * @Date 2022/4/1 6:46 PM
 */
@Slf4j
public class MessageEventListener implements EventListener {
    @Override
    public void doEvent(LotteryResult result) {
        log.info("给用户{}发送短信通知（短信）：{}", result.getUid(),
                result.getMsg());
    }
}
