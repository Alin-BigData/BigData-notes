package com.wangfulin.ch2.tools.semaphore;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * @projectName: concurrent
 * @description: 演示Semaphore用法，一个数据库连接池的实现
 * @author: Wangfulin
 * @create: 2020-04-19 11:15
 **/
public class DBPoolSemaphore {
    private final static int POOL_SIZE = 10;
    private final Semaphore useful, useless;//useful表示可用的数据库连接，useless表示已用的数据库连接

    public DBPoolSemaphore() {
        this.useful = new Semaphore(POOL_SIZE); //可用的数据库连接
        this.useless = new Semaphore(0); //已用的数据库连接
    }

    //存放数据库连接的容器
    private static LinkedList<Connection> pool = new LinkedList<Connection>();

    //初始化池
    static {
        for (int i = 0; i < POOL_SIZE; i++) {
            pool.addLast(SqlConnectImpl.fetchConnection());
        }
    }

    /*归还连接*/
    public void returnConnect(Connection connection) throws InterruptedException {
        if (connection != null) {
            System.out.println("当前有" + useful.getQueueLength() + "个线程等待数据库连接！！"
                    + "可用连接数:" + useful.availablePermits());
            useless.acquire(); // 申请已用连接的许可证
            synchronized (pool) {
                pool.addLast(connection);
            }
            useful.release();// 可用连接放回了一个
        }
    }

    /*从池子拿连接*/
    public Connection takeConnect() throws InterruptedException {
        useful.acquire(); //拿连接，需要拿到可用的许可 useful是空的，则可用的连接阻塞，
        Connection conn;
        synchronized (pool) {
            conn = pool.removeFirst(); // 从池中取走一个
        }
        useless.release(); // 已用连接释放，表示已用连接已经多了一个
        return conn;
    }

}
