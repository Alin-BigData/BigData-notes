package com.wangfulin.ch3.answer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @projectName: concurrent
 * @description: 有一个残缺AtomicInteger的类实现了线程安全的：
 * *get方法和compareAndSet()方法
 * *自行实现它的递增方法
 * @author: Wangfulin
 * @create: 2020-04-20 12:59
 **/
public class HalfAtomicInt {
    private AtomicInteger atomicI = new AtomicInteger(0);

    public void increament() {
        // 为什么在for循环里面取值，是为了i值不变化
        for (; ; ) {
            int i = atomicI.get();
            boolean suc = atomicI.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }

    public int getCount() {
        return atomicI.get();
    }
}
