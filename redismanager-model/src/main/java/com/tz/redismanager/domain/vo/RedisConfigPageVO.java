package com.tz.redismanager.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>redis连接分页数据VO</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-28 23:24
 **/
@Getter
@Setter
public class RedisConfigPageVO {

    /**
     * redis连接配置列表
     */
    List<?> configList = new ArrayList<>();

    /**
     * 当前redis连接配置列表中最小的id<br/>
     * v1.7.0-20211109-需要把currentMinId属性的类型从Long修改为String，<br/>
     * 因为js的number类型有个最大值（安全值）。即2的53次方=9007199254740992，如果超过这个值，那么js会出现不精确的问题，<br/>
     * 例如：数据库中的id=21827466180016128，但是页面中展示的id=21827466180016130<br/>
     */
    String currentMinId = "0";
}
