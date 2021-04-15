package com.tz.redismanager.config.event;

import com.tz.redismanager.config.domain.po.ConfigPO;
import org.springframework.context.ApplicationEvent;

/**
 * <p>配置修改事件</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 23:04
 **/
public class ConfigUpdateEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ConfigUpdateEvent(ConfigPO source) {
        super(source);
    }
}
