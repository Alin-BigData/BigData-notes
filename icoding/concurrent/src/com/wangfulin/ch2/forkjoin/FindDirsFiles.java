package com.wangfulin.ch2.forkjoin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * @projectName: concurrent
 * @description: 遍历指定目录（含子目录）找寻指定类型文件 使用forkjoin的异步方法
 * @author: Wangfulin
 * @create: 2020-04-18 16:52
 **/
public class FindDirsFiles extends RecursiveAction {

    private File path;

    public FindDirsFiles(File path) {
        this.path = path;
    }

    @Override
    protected void compute() {
        List<FindDirsFiles> subTasks = new ArrayList<>();

        File[] files = path.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {//如果是文件夹，则丢给子任务继续递归
                    subTasks.add(new FindDirsFiles(file));
                } else {
                    // 单纯文件
                    if (file.getAbsolutePath().endsWith(".md")) {
                        System.out.println("文件：" + file.getAbsolutePath());
                    }
                }
            }
            // 当子任务非空 需要搜寻的时候
            if (!subTasks.isEmpty()) { // 如果子任务非空
                // invokeAll返回值是一个集合 把返回值作为循环的主要部分
                for (FindDirsFiles subTask : invokeAll(subTasks)) {
                    subTask.join();//等待子任务执行完成
                }
            }
        }

    }

    public static void main(String[] args) {
        try {
            // 用一个 ForkJoinPool 实例调度总任务
            ForkJoinPool pool = new ForkJoinPool();
            FindDirsFiles task = new FindDirsFiles(new File("/Users/wangfulin"));

            pool.execute(task);//异步调用

            System.out.println("Task is Running......");
            Thread.sleep(1);
            int otherWork = 0;
            for (int i = 0; i < 100; i++) {
                otherWork = otherWork + i;
            }
            System.out.println("Main Thread done sth......,otherWork=" + otherWork);
            task.join();//阻塞的方法 等待task完成工作
            System.out.println("Task end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
