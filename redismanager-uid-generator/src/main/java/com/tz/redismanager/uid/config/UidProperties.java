package com.tz.redismanager.uid.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>uid属性配置</p>
 *
 * 例子1：对于并发数要求不高、期望长期使用的应用, 可增加timeBits位数, 减少seqBits位数.
 * 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为12次/天,
 * 那么配置成{"timeBits":31,"workerBits":23,"seqBits":9}时, 可支持28个节点以整体并发量14400 UID/s的速度持续运行68年.
 * --->2^31÷365天÷24小时÷60分钟÷60秒≈68年;
 * --->2^23÷68年÷365天÷28个节点≈12次/天
 * --->2^9*28个节点≈14336 UID/s
 * <br/>
 * 例子2：对于节点重启频率频繁、期望长期使用的应用, 可增加workerBits和timeBits位数, 减少seqBits位数.
 * 例如节点采取用完即弃的WorkerIdAssigner策略, 重启频率为24*12次/天,
 * 那么配置成{"timeBits":30,"workerBits":27,"seqBits":6}时, 可支持37个节点以整体并发量2400 UID/s的速度持续运行34年.
 * --->2^30÷365天÷24小时÷60分钟÷60秒≈34年;
 * --->2^27÷34年÷365天÷37个节点≈292次/天
 * --->2^6*37个节点≈2368 UID/s
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-22 22:58
 **/
@ConfigurationProperties(prefix = "rm.uid")
@Getter
@Setter
public class UidProperties {

    /**
     * 2^31/365/24/60/60≈68年
     * 单位：秒，最多可支持约68年
     */
    private int timeBits = 31;

    /**
     * 2^21≈200W
     * 最多可支持总共约200W次机器启动或者平均每天84次重启
     */
    private int workerBits = 21;

    /**
     * 2^11=2048
     * 可支持每秒2048个并发
     */
    private int seqBits = 11;

    /**
     * 时间基点
     */
    private String epochStr = "2021-08-22";

}
