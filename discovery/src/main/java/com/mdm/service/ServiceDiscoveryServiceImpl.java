package com.mdm.service;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;

/**
 * @author dongming.ma
 * @date 2022/5/28 19:12
 */
public class ServiceDiscoveryServiceImpl {

    private final CloseableHttpAsyncClient asyncClient;

    private final DefaultConnectingIOReactor ioreactor;

    private final RequestConfig defaultConfig;

    public ServiceDiscoveryServiceImpl(CloseableHttpAsyncClient asyncClient, DefaultConnectingIOReactor ioreactor, RequestConfig defaultConfig) {
        this.asyncClient = asyncClient;
        this.ioreactor = ioreactor;
        this.defaultConfig = defaultConfig;
    }
}
