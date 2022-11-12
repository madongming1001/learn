package com.madm.learnroute.technology.zookeeper;

import lombok.Data;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

@Data
public class ZKServiceProvider implements Watcher {
    private static CountDownLatch connectedSempahore = new CountDownLatch(1);
    private static ZooKeeper zk = null;
    private static String rootPath = "/GroupMembers";
    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);

    public static void main(String[] args) throws Exception {
//        zk = new ZooKeeper("localhost:2181", 5000, new ZKServiceProvider());
        build();
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

    public static void build() {
        CuratorFrameworkFactory.builder().connectString("0.0.0.0:8081").sessionTimeoutMs(5000)  // 会话超时时间
                .connectionTimeoutMs(5000) // 连接超时时间
                .retryPolicy(retryPolicy).namespace("base") // 包含隔离名称
                .build().start();
    }
}
