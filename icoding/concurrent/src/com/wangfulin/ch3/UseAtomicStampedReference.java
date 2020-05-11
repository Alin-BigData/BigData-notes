package com.wangfulin.ch3;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @projectName: concurrent
 * @description: 带版本戳的原子操作类 关心动用过几次
 * @author: Wangfulin
 * @create: 2020-04-19 20:31
 **/
public class UseAtomicStampedReference {
    static AtomicStampedReference<String> asr =
            new AtomicStampedReference<>("Mark", 0);

    public static void main(String[] args) throws InterruptedException {
        final int oldStamp = asr.getStamp(); //那初始的版本号
        final String oldReferenc = asr.getReference(); // 原值

        System.out.println(oldReferenc + "===========" + oldStamp);

        // 内部类
        Thread rightStampThread = new Thread(new Runnable() {


            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()
                        + "当前变量值：" + oldReferenc + "当前版本戳：" + oldStamp + "-"
                        + asr.compareAndSet(oldReferenc, oldReferenc + "Java",
                        oldStamp, oldStamp + 1));

            }
        });

        Thread errorStampThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // 虽然值是引用旧的值，但是版本号因为已经改变，但是这里依旧取了oldStamp，因此会出现错误
                String reference = asr.getReference();
                System.out.println(Thread.currentThread().getName()
                        + "当前变量值：" + reference + "当前版本戳：" + asr.getStamp() + "-"
                        + asr.compareAndSet(reference, reference + "C",
                        oldStamp, oldStamp + 1));

            }

        });

        // 先启动正确版本戳 运行完 因为正确版本运行完，版本号变了，因此错误版本会出现错误
        rightStampThread.start();
        rightStampThread.join();
        errorStampThread.start();
        errorStampThread.join();
        System.out.println(asr.getReference() + "===========" + asr.getStamp());
    }
}
