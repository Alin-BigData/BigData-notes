package com.wangfulin.ch3;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @projectName: concurrent
 * @description: 原子数组
 * @author: Wangfulin
 * @create: 2020-04-19 20:44
 **/
public class AtomicArray {
    static int[] value = new int[]{1, 2};

    static AtomicIntegerArray ai = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        // i是当前数组的下标
        ai.getAndSet(0, 3);
        System.out.println(ai.get(0));
        System.out.println(value[0]);

    }
}

