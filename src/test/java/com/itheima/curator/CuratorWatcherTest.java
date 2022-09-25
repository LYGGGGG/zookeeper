package com.itheima.curator;

/*
@author YG
@create 2022/9/25   12:31
*/

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorWatcherTest {
    private CuratorFramework client;

    /**
     * 建立连接
     */
    @Before
    public void testConnect() {

        /*
         * @param connectString       连接字符串。zk server 地址和端口 "192.168.149.135:2181,192.168.149.136:2181"
         * @param sessionTimeoutMs    会话超时时间 单位ms
         * @param connectionTimeoutMs 连接超时时间 单位ms
         * @param retryPolicy         重试策略
         */
       /* //重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000,10);
        //1.第一种方式
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.149.135:2181",
                60 * 1000, 15 * 1000, retryPolicy);*/
        //重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(300, 1);
        //2.第二种方式
        //CuratorFrameworkFactory.builder();
        client = CuratorFrameworkFactory.builder()
                .connectString("192.168.42.100:2181")
                .sessionTimeoutMs(1 * 100)
                .connectionTimeoutMs(1 * 100)
                .retryPolicy(retryPolicy)
                .namespace("itheima")
                .build();

        //开启连接
        client.start();

    }

    @Test
    public void testNodeCache() throws Exception {
        //1.创建监听对象
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, "/app1", true);
        //2.注册监听！
        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if (pathChildrenCacheEvent.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)){
                    System.out.println("/itheima/app1的子节点数据更新");
                    System.out.println(new String(pathChildrenCacheEvent.getData().getData()));
                }
            }
        });
        //3. 开启监听，如果设置为true，则开启监听是，加载缓冲数据
        pathChildrenCache.start(true);
        while (true){

        }
    }

    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}
