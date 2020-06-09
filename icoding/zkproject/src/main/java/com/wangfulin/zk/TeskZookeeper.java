package com.wangfulin.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;
import java.util.List;

/**
 * @projectName: zkproject
 * @description: TODO
 * @author: Wangfulin
 * @create: 2020-06-09 16:42
 **/
public class TeskZookeeper {
    private static String connectString = "127.0.0.1:2181";
    // 会话超时时间 2s
    private static int sessionTimeout = 2000;
    private ZooKeeper zkClient = null;

    @Before
    public void init() throws IOException {
        new ZooKeeper(connectString, sessionTimeout, new Watcher() {

            public void process(WatchedEvent event) {
                System.out.println("---------start----------");
                List<String> children;
                try {
                    children = zkClient.getChildren("/", true);

                    for (String child : children) {
                        System.out.println(child);
                    }
                    System.out.println("---------end----------");
                } catch (KeeperException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
    }

    @Test
    public void createNode() throws KeeperException, InterruptedException {
        // 参数1：要创建的节点的路径； 参数2：节点数据 ； 参数3：节点权限 ；参数4：节点的类型
        String nodeCreated = zkClient.create("/wangfulin", "hello".getBytes(),
                Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println(nodeCreated);
    }

    // 2 获取子节点 并监控节点的变化
    @Test
    public void getDataAndWatch() throws KeeperException, InterruptedException {

        List<String> children = zkClient.getChildren("/", true);

        for (String child : children) {
            System.out.println(child);
        }

        Thread.sleep(Long.MAX_VALUE);
    }

    // 3 判断节点是否存在
    @Test
    public void exist() throws KeeperException, InterruptedException {

        Stat stat = zkClient.exists("/wangfulin", false);

        System.out.println(stat == null ? "not exist" : "exist");
    }


}
