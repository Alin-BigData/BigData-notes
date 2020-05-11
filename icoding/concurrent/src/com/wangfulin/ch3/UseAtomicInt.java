package com.wangfulin.ch3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @projectName: concurrent
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-04-19 20:15
 **/
public class UseAtomicInt {

    static AtomicInteger ai = new AtomicInteger(10);

    public static void main(String[] args) {
        System.out.println(ai.getAndIncrement()); // 10--->11 先取旧值，再增
        System.out.println(ai.incrementAndGet()); // 11--->12--->out 先增，再取
        System.out.println(ai.get());
    }
}
