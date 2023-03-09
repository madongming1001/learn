package com.madm.learnroute.javaee.concurrent.util;

import java.util.concurrent.Phaser;

/**
 * @author dongming.ma
 * @date 2023/3/9 10:43
 */
public class ReptilePhaser extends Phaser {

    public ReptilePhaser(int parties) {
        super(parties);
    }

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case 0:
                System.out.println("第一阶段，收集完成！参与者数量：" + registeredParties);
                break;
            case 1:
                System.out.println("第二阶段，整理完成！参与者数量：" + registeredParties);
                break;
            default:
                break;
        }
        return super.onAdvance(phase, registeredParties);
    }
}
