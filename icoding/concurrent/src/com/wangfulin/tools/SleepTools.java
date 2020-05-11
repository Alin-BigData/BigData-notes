package com.wangfulin.tools;

import java.util.concurrent.TimeUnit;

/**
 * @projectName: concurrent
 * @description: 休眠工具类
 * @author: Wangfulin
 * @create: 2020-04-17 13:58
 **/
public class SleepTools {
    /**
     * 按秒休眠
     * @param seconds 秒数
     */
    public static final void second(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * 按毫秒数休眠
     * @param seconds 毫秒数
     */
    public static final void ms(int seconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }
}
