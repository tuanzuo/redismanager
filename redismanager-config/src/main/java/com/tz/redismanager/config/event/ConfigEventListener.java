package com.tz.redismanager.config.event;

import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.notify.INotiyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 23:07
 **/
@Service
public class ConfigEventListener {

    @Autowired
    private INotiyService notiyService;

    @EventListener({ConfigAddEvent.class})
    public void addEentHandle(ConfigAddEvent event) {
        ConfigPO po = ConfigPO.class.cast(event.getSource());
        notiyService.add(po);
    }

    @EventListener({ConfigUpdateEvent.class})
    public void updateEventHandle(ConfigUpdateEvent event) {
        ConfigPO po = ConfigPO.class.cast(event.getSource());
        notiyService.update(po);
    }
}