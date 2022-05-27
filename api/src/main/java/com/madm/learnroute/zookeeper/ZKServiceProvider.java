package com.madm.learnroute.zookeeper;

import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

public class ZKServiceProvider implements Watcher {
    private static CountDownLatch connectedSempahore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static String rootPath = "/GroupMembers";

    public static void main(String[] args) throws Exception {
        zk = new ZooKeeper("localhost:2181", 5000, new ZKServiceProvider());
        connectedSempahore.await();
        zk.create(rootPath + "/c1", "test create".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("创建集群节点c1：" + rootPath + "/c1");
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Override
    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSempahore.countDown();
            }
        }
    }
}
