package com.wangfulin.ch1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @projectName: concurrent
 * @description: 如何新建线程
 * @author: Wangfulin
 * @create: 2020-04-16 22:46
 **/
public class NewThread {
    /*扩展自Thread类*/

    /*实现Runnable接口*/
    private static class UseRunable implements Runnable {

        @Override
        public void run() {
            System.out.println("I am implements Runnable");
        }
    }

    /*实现Callable接口，允许有返回值*/
    private static class UseCall implements Callable<String> {

        @Override
        public String call() throws Exception {
            System.out.println("I am implements Callable");
            return "CallResult";
        }
    }

    public static void main(String[] args)
            throws InterruptedException, ExecutionException {

        UseRunable useRun = new UseRunable();
        new Thread(useRun).start();
        Thread t = new Thread(useRun);
        t.interrupt();

        // callable 返回值 用FutureTask包装，FutureTask实现了Runable接口
        // 可以将FutureTask当作一个Runable，递交给线程去运行
        UseCall useCall = new UseCall();
        FutureTask<String> futureTask = new FutureTask<>(useCall);
        new Thread(futureTask).start();
        System.out.println(futureTask.get());
    }
}
