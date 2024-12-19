package com.mdm.headfirst.remote;

/**
 * 复习java第一天 复习抽象类如何进行方法必须重写吗
 */
public abstract class RemoteControl {
    TV tv;
    TVFactory tvFactory;

    public RemoteControl(TVFactory tvFactory) {
        this.tvFactory = tvFactory;
    }

    public void on() {
        this.tv.on();
    }

    public void off() {
        this.tv.off();
    }

    public int getChannel() {
        return tv.getChannel();
    }

    public void setChannel(int channel) {
        tv.tuneChannel(channel);
    }

    public void setTV(String type) {
        try {
            tv = tvFactory.getTV(type);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}