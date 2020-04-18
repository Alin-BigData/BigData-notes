package com.wangfulin.ch1.pool;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * @projectName: concurrent
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-04-17 17:59
 **/
public class DBPool {
    //数据库池的容器
    private static LinkedList<Connection> pool = new LinkedList<>();

    // 线程池 连接数量
    public DBPool(int initalSize) {
        if (initalSize > 0) {
            for (int i = 0; i < initalSize; i++) {
                pool.addLast(SqlConnectImpl.fetchConnection());
            }
        }
    }

    //在mills时间内还拿不到数据库连接，返回一个null 等待超时模式
    public Connection fetchConn(long mills) throws InterruptedException {
        synchronized (pool) {
            // 代表永不超时
            if (mills < 0) {
                while (pool.isEmpty()) {
                    pool.wait();
                }
                return pool.removeFirst();
            } else { // 进入等待超时模式
                long overtime = System.currentTimeMillis() + mills;
                long remain = mills; // 持续时间
                while (pool.isEmpty() && remain > 0) {
                    pool.wait(remain);
                    remain = overtime - System.currentTimeMillis(); //剩余持续时间
                }
                Connection result = null;
                // 尝试拿一下池里面取一个连接
                if (!pool.isEmpty()) {
                    result = pool.removeFirst();
                }
                return result;
            }
        }
    }

    //释放数据库连接
    public void releaseConn(Connection conn) {
        if (conn != null) {
            synchronized (pool) {
                pool.addLast(conn);
                // 通知可以取
                pool.notifyAll();
            }
        }
    }

}
