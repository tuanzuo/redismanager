package com.tz.redismanager.config.sdk.listener.zookeeper.curator;

import lombok.Getter;
import lombok.Setter;
import org.apache.curator.framework.CuratorFramework;

/**
 * <p>CuratorFramework的组合</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 19:45
 **/
@Setter
@Getter
public class CuratorFrameworkCompose {

    public CuratorFrameworkCompose(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    private CuratorFramework curatorFramework;

}
