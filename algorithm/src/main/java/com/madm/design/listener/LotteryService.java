package com.madm.design.listener;

/**
 * @Author MaDongMing
 * @Date 2022/4/1 6:50 PM
 */
public abstract class LotteryService {

    private EventManager eventManager;

    public LotteryService() {
        eventManager = new EventManager(EventManager.EventType.MQ,
                EventManager.EventType.Message);
        eventManager.subscribe(EventManager.EventType.MQ, new
                MQEventListener());
        eventManager.subscribe(EventManager.EventType.Message, new
                MessageEventListener());
    }

    public LotteryResult draw(String uId) {
        LotteryResult lotteryResult = doDraw(uId);
        //需要什么通知就给调用什么方法
        eventManager.notify(EventManager.EventType.MQ, lotteryResult);
        eventManager.notify(EventManager.EventType.Message,
                lotteryResult);
        return lotteryResult;
    }

    protected abstract LotteryResult doDraw(String uId);
}
