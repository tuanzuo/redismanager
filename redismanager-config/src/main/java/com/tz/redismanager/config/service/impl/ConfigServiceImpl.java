package com.tz.redismanager.config.service.impl;

import com.tz.redismanager.config.dao.IConfigDao;
import com.tz.redismanager.config.domain.dto.ConfigDTO;
import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.enm.ConfigTypeEnum;
import com.tz.redismanager.config.event.ConfigAddEvent;
import com.tz.redismanager.config.event.ConfigUpdateEvent;
import com.tz.redismanager.config.exception.ConfigException;
import com.tz.redismanager.config.service.IConfigService;
import com.tz.redismanager.config.util.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * <p>配置服务实现</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-15 22:40
 **/
public class ConfigServiceImpl implements IConfigService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private IConfigDao configDao;
    private ApplicationContext applicationContext;

    public ConfigServiceImpl(IConfigDao configDao, ApplicationContext applicationContext) {
        this.configDao = configDao;
        this.applicationContext = applicationContext;
    }

    @Override
    public List<ConfigPO> queryPageList(ConfigPageParam param) {
        return configDao.selectPageByParam(param);
    }

    @Override
    public List<ConfigPO> queryList(ConfigQueryParam param) {
        return configDao.selectListByParam(param);
    }

    @Override
    public int count(ConfigQueryParam param) {
        return configDao.count(param);
    }

    @Override
    public int countPage(ConfigPageParam param) {
        return configDao.countPage(param);
    }

    @Override
    public int addConfig(ConfigPO configPO) {
        int count = configDao.count(this.buildConfigQueryParam(configPO));
        if (count > 0) {
            logger.error("[config配置] [添加配置] [已存在相同配置不能重复添加] serviceName:{},configType:{},configKey:{}", configPO.getServiceName(), configPO.getConfigType(), configPO.getConfigKey());
            throw new ConfigException("配置重复[服务名=" + configPO.getServiceName() + ",配置类型=" + ConfigTypeEnum.getByCode(configPO.getConfigType()).getMsg() + ",配置Key=" + configPO.getConfigKey() + "]");
        }
        int retVal = configDao.insert(configPO);
        if (retVal > 0) {
            applicationContext.publishEvent(new ConfigAddEvent(configPO));
        } else {
            logger.error("[config配置] [添加配置] [失败] ConfigPO:{}", JsonUtils.toJsonStr(configPO));
            throw new ConfigException("配置添加失败");
        }
        return retVal;
    }

    @Override
    public int updateConfig(ConfigPO configPO) {
        List<ConfigPO> list = configDao.selectListByParam(this.buildConfigQueryParam(configPO));
        if(CollectionUtils.isNotEmpty(list)){
            long existCount = list.stream().filter(temp -> !temp.getId().equals(configPO.getId())).count();
            if (existCount > 0) {
                logger.error("[config配置] [修改配置] [已存在相同配置不能修改] serviceName:{},configType:{},configKey:{}", configPO.getServiceName(), configPO.getConfigType(), configPO.getConfigKey());
                throw new ConfigException("配置重复[服务名=" + configPO.getServiceName() + ",配置类型=" + ConfigTypeEnum.getByCode(configPO.getConfigType()).getMsg() + ",配置Key=" + configPO.getConfigKey() + "]");
            }
        }
        int retVal = configDao.updateByPrimaryKey(configPO);
        if (retVal > 0) {
            applicationContext.publishEvent(new ConfigUpdateEvent(configPO));
        } else {
            logger.error("[config配置] [修改配置] [失败] ConfigPO:{}", JsonUtils.toJsonStr(configPO));
            throw new ConfigException("配置修改失败");
        }
        return retVal;
    }

    @Override
    public int delConfig(ConfigDTO dto) {
        int retVal = configDao.deleteLogicByIds(dto);
        if (retVal <= 0) {
            logger.error("[config配置] [删除配置] [失败] ConfigDTO:{}", JsonUtils.toJsonStr(dto));
            throw new ConfigException("配置删除失败");
        }
        return retVal;
    }

    private ConfigQueryParam buildConfigQueryParam(ConfigPO configPO) {
        ConfigQueryParam queryParam = new ConfigQueryParam();
        queryParam.setServiceName(configPO.getServiceName());
        queryParam.setConfigType(configPO.getConfigType());
        queryParam.setConfigKey(configPO.getConfigKey());
        return queryParam;
    }
}
