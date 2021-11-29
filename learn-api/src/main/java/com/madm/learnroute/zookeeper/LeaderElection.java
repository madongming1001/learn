package com.madm.learnroute.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class LeaderElection {
    private final static String CONNECT_PATH = "192.168.109.200:2181";
    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    private static CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(CONNECT_PATH, retryPolicy);

    public static void main(String[] args) {
        curatorFramework.start();
        LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework client) throws Exception {
                // this callback will get called when you are the leader
                // do whatever leader work you need to and only exit
                // this method when you want to relinquish leadership
            }
        };
        LeaderSelector selector = new LeaderSelector(curatorFramework, CONNECT_PATH, listener);
        selector.autoRequeue();  // not required, but this is behavior that you will probably expect
        selector.start();
    }
}
