package com.madm.learnroute.zookeeper;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ZookeeperConfig {
    private String key;
    private String name;
}
