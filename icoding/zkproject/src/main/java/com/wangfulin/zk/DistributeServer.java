package com.wangfulin.zk;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * @projectName: zkproject
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-09 20:14
 **/
public class DistributeServer {
    private String connectString = "localhost:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;

    public static void main(String[] args) throws Exception {

        DistributeServer server = new DistributeServer();

        // 1 连接zookeeper集群
        server.getConnect();

        // 2 注册节点
        server.regist(args[0]);

        // 3 业务逻辑处理
        server.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    // -e -s 类型 带序号且临时的
    private void regist(String hostname) throws KeeperException, InterruptedException {
        String path = zkClient.create("/servers/server", hostname.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

        System.out.println(hostname + "is online ");
    }

    private void getConnect() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {


            public void process(WatchedEvent event) {
                // TODO Auto-generated method stub

            }
        });
    }

}
