package com.madm.learnroute.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class ConfigCenter {
    private final static String CONNECT_STR = "192.168.109.200:2181";
    private final static Integer SESSION_TIMEOUT = 30 * 1000;
    private static ZooKeeper zookeeper = null;
    private static CountDownLatch countDownLatch = new CountDownLatch(1);
    public static void main(String[] args) throws Exception {
        zookeeper = new ZooKeeper(CONNECT_STR, SESSION_TIMEOUT, (event) -> {
            if (event.getType() == Watcher.Event.EventType.None && event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                countDownLatch.countDown();
                log.info("连接已建立");
            }
        });
        countDownLatch.await();


    }
}
