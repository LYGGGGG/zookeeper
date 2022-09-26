package com.itheima.curator.distributed_lock;

/*
@author YG
@create 2022/9/27   2:47
*/

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.TimeUnit;

public class Ticket12306 implements Runnable {
    private int tickets = 30;
    private InterProcessMutex lock;

    public Ticket12306() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 10);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("192.168.42.100:2181")
                .sessionTimeoutMs(10 * 1000)
                .connectionTimeoutMs(10 * 1000)
                .retryPolicy(retryPolicy)
                .build();
        client.start();

        lock = new InterProcessMutex(client, "/itheima");
    }

    @Override
    public void run() {
        while (true) {
            try {
                //获取锁
                lock.acquire(1, TimeUnit.SECONDS);
                if (tickets > 0) {
                    System.out.println(Thread.currentThread() + ":" + tickets);
                    tickets--;
                    Thread.sleep(500);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    //释放锁
                    lock.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
