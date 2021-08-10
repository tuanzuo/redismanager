package com.tz.redismanager.config.event;

import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.notify.INotiyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * <p>配置事件监听器</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 23:07
 **/
@Service
public class ConfigEventListener {

    @Autowired
    private INotiyService notiyService;

    /**
     * 配置添加事件监听
     *
     * @param event
     */
    @EventListener({ConfigAddEvent.class})
    public void addEentHandle(ConfigAddEvent event) {
        ConfigPO po = ConfigPO.class.cast(event.getSource());
        notiyService.add(po);
    }

    /**
     * 配置修改事件监听
     *
     * @param event
     */
    @EventListener({ConfigUpdateEvent.class})
    public void updateEventHandle(ConfigUpdateEvent event) {
        ConfigPO po = ConfigPO.class.cast(event.getSource());
        notiyService.update(po);
    }
}
