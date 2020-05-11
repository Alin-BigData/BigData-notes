package com.wangfulin.ch4;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @projectName: concurrent
 * @description: 使用显示锁的范式
 * @author: Wangfulin
 * @create: 2020-04-20 15:23
 **/
public class LockDemo {
    private Lock lock  = new ReentrantLock();
    private int count;

    public void increament() {
        lock.lock();
        try{
            count++;
        } finally {
            lock.unlock();
        }
    }
}
