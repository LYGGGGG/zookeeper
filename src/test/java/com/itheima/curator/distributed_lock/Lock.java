package com.itheima.curator.distributed_lock;

/*
@author YG
@create 2022/9/27   2:50
*/

public class Lock {
    public static void main(String[] args) {
        Ticket12306 ticket12306 = new Ticket12306();
        new Thread(ticket12306,"协程").start();
        new Thread(ticket12306,"飞猪").start();
        new Thread(ticket12306,"去哪").start();
        new Thread(ticket12306,"官网").start();
    }
}
