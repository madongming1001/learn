package madm.design.listener;

import java.util.Date;

/**
 * @Author MaDongMing
 * @Date 2022/4/1 6:50 PM
 */
public class LotteryServiceImpl extends LotteryService {

    private MinibusTargetService minibusTargetService = new
            MinibusTargetService();

    @Override
    protected LotteryResult doDraw(String uid) {
        String lottery = minibusTargetService.lottery(uid);
        return new LotteryResult(uid, lottery, new Date());
    }
}
