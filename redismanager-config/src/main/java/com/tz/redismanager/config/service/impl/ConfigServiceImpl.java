package com.tz.redismanager.config.service.impl;

import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.domain.dto.ConfigDTO;
import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.event.ConfigAddEvent;
import com.tz.redismanager.config.event.ConfigUpdateEvent;
import com.tz.redismanager.config.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 22:40
 **/
@Service
public class ConfigServiceImpl implements IConfigService {

    @Autowired
    private IConfigDao configDao;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public List<ConfigPO> queryList(ConfigPageParam param) {
        return configDao.selectPageByParam(param);
    }

    @Override
    public int count(ConfigPageParam param) {
        return configDao.count(param);
    }

    @Override
    public void addConfig(ConfigPO configPO) {
        configDao.insert(configPO);
        applicationContext.publishEvent(new ConfigAddEvent(configPO));
    }

    @Override
    public void updateConfig(ConfigPO configPO) {
        configDao.updateByPrimaryKey(configPO);
        applicationContext.publishEvent(new ConfigUpdateEvent(configPO));
    }

    @Override
    public void delConfig(ConfigDTO dto) {
        configDao.deleteLogicByIds(dto);
    }
}
