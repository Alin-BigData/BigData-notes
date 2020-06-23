package com.wangfulin.zk;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @projectName: zkproject
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-09 20:20
 **/
public class DistributeClient {
    private String connectString = "localhost:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {

        DistributeClient client = new DistributeClient();



        // 1 获取zookeeper集群连接
        client.getConnect();

        // 2 注册监听
        client.getChlidren();

        // 3 业务逻辑处理
        client.business();






    }




    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    // 获取子结点
    private void getChlidren() throws KeeperException, InterruptedException {
        // server1 server2 server3
        List<String> children = zkClient.getChildren("/servers", true);

        // 用集合 存储服务器节点主机名称
        ArrayList<String> hosts = new ArrayList<String>();

        for (String child : children) {

            byte[] data = zkClient.getData("/servers/" + child, false, null);

            hosts.add(new String(data));
        }

        // 将所有在线主机名称打印到控制台
        System.out.println(hosts);
    }


    private void getConnect() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {

            // 监听 不断死循环 Watcher里面的process，因此要把 getChlidren写在Watcher里面

            public void process(WatchedEvent event) {

                try {
                    getChlidren();
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
