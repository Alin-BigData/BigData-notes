package com.wangfulin.ch2.forkjoin;

import java.util.Random;

/**
 * @projectName: concurrent
 * @description: 产生整形数组
 * @author: Wangfulin
 * @create: 2020-04-18 01:16
 **/
public class MakeArray {
    public static final int ARRAY_LENGTH = 1000;

    public static int[] makeArray() {
        Random r = new Random();
        int[] result = new int[ARRAY_LENGTH];
        for (int i = 0; i < ARRAY_LENGTH; i++) {
            //用随机数填充数组
            result[i] =  r.nextInt(ARRAY_LENGTH*3);
        }
        return result;
    }
}
