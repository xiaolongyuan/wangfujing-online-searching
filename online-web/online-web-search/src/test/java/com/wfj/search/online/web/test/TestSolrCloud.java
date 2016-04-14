package com.wfj.search.online.web.test;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <br/>create at 15-12-23
 *
 * @author liuxh
 * @since 1.0.0
 */
@Ignore
public class TestSolrCloud {
    @Test
    public void test() throws Exception {
        String zkAddresses = "10.6.2.55:12181";
        String statusPath = "/com/wfj/search/test/node";
        CuratorFramework client = CuratorFrameworkFactory
                .builder()
                .connectString(zkAddresses)
                .retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        if (client.getState() == CuratorFrameworkState.LATENT) {
            client.start();
        }
        if (client.getState() == CuratorFrameworkState.STARTED) {
            try {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(statusPath);
            } catch (Exception e) {
                if (client.checkExists().forPath(statusPath) == null) {
                    throw e;
                }
            }
        } else if (client.getState() == CuratorFrameworkState.STOPPED) {
            throw new IllegalStateException("ZooKeeper has disconnected");
        }
    }

    @Test
    public void test2() throws Exception {
        ZooKeeper zk = new ZooKeeper("10.6.2.55:12181", 3000, null);
        System.out.println("=========创建节点===========");
        if (zk.exists("/test", false) == null) {
            zk.create("/test", "znode1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        System.out.println("=============查看节点是否安装成功===============");
        System.out.println(new String(zk.getData("/test", false, null)));

        System.out.println("=========修改节点的数据==========");
        zk.setData("/test", "zNode2".getBytes(), -1);
        System.out.println("========查看修改的节点是否成功=========");
        System.out.println(new String(zk.getData("/test", false, null)));

        System.out.println("=======删除节点==========");
        zk.delete("/test", -1);
        System.out.println("==========查看节点是否被删除============");
        System.out.println("节点状态：" + zk.exists("/test", false));
        zk.close();
    }
}
