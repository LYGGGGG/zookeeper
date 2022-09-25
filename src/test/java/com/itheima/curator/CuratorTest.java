package com.itheima.curator;

/*
@author YG
@create 2022/9/25   12:31
*/

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CuratorTest {
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
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        //2.第二种方式
        //CuratorFrameworkFactory.builder();
        client = CuratorFrameworkFactory.builder()
                .connectString("192.168.42.100:2181")
                .sessionTimeoutMs(10 * 1000)
                .connectionTimeoutMs(10 * 1000)
                .retryPolicy(retryPolicy)
                .namespace("itheima")
                .build();

        //开启连接
        client.start();

    }
//增删改查
//==============================create=============================================================================

    /**
     * 创建节点：create 持久 临时 顺序 数据
     * 1. 基本创建 ：create().forPath("")，没有指定数据，则默认将当前客户端的ip作为数据存储
     * 2. 创建节点 带有数据:create().forPath("",data)
     * 3. 设置节点的类型：create().withMode().forPath("",data)【错误】
     * 4. 创建多级节点  /app1/p1 ：create().creatingParentsIfNeeded().forPath("",data)
     */
    @Test
    public void testCreate() throws Exception {
        String path = client.create().creatingParentsIfNeeded().forPath("/app5/yg1");
        System.out.println(path);
    }

    //===========================get================================================================================

    /**
     * 查询节点：
     * 1. 查询数据：get: getData().forPath()
     * 2. 查询子节点： ls: getChildren().forPath()
     * 3. 查询节点状态信息：ls -s:getData().storingStatIn(状态对象).forPath()
     */

    @Test
    public void testGet() throws Exception {
//        Stat stat = new Stat();
//        System.out.println(stat);
//        client.getData().storingStatIn(stat).forPath("/app2");
//
//        List<String> list = client.getChildren().forPath("/");
//        System.out.println(list);

        byte[] bytes = client.getData().forPath("/app2");
        System.out.println(new String(bytes));


    }
    //===========================set================================================================================

    /**
     * 修改数据
     * 1. 基本修改数据：setData().forPath()
     * 2. 根据版本修改: setData().withVersion().forPath()
     * * version 是通过查询出来的。目的就是为了让其他客户端或者线程不干扰我。
     *
     * @throws Exception
     */
    @Test
    public void testSet() throws Exception {
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/app2");
        int version = stat.getVersion();
        client.setData().withVersion(version).forPath("/app2", "zyyyyy".getBytes());
    }
    //===========================delete================================================================================

    /**
     * 删除节点： delete deleteall
     * 1. 删除单个节点:delete().forPath("/app1");
     * 2. 删除带有子节点的节点:delete().deletingChildrenIfNeeded().forPath("/app1");
     * 3. 必须成功的删除:为了防止网络抖动。本质就是重试。  client.delete().guaranteed().forPath("/app2");
     * 4. 回调：inBackground
     *
     * @throws Exception
     */
    @Test
    public void testDelete() throws Exception {
        client.delete().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework c, CuratorEvent curatorEvent) throws Exception {
                System.out.println("删除成功");
                System.out.println(curatorEvent);
                System.out.println("///////////////");
                System.out.println(c.getData().forPath("/app2"));
            }
        }).forPath("/app2");
    }

    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}
