package com.tz.redismanager.configure;

import com.tz.redismanager.config.domain.param.ConfigQueryParam;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.sdk.domain.dto.ConfigContext;
import com.tz.redismanager.config.sdk.domain.dto.ConfigQueryDTO;
import com.tz.redismanager.config.sdk.service.IFetchConfigService;
import com.tz.redismanager.config.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-07 20:16
 **/
@Service
public class FetchConfigServiceImpl implements IFetchConfigService {

    @Autowired
    private IConfigService configService;

    @Override
    public List<ConfigContext> fetchConfig(ConfigQueryDTO dto) {
        List<ConfigContext> configContextList = new ArrayList<>();
        List<ConfigPO> list = Optional.ofNullable(configService.queryList(this.buildConfigQueryParam(dto))).orElse(new ArrayList<>());
        list.forEach(temp -> {
            configContextList.add(this.buildConfigContext(temp));
        });
        return configContextList;
    }

    private ConfigContext buildConfigContext(ConfigPO temp) {
        ConfigContext configContext = new ConfigContext();
        configContext.setId(temp.getId());
        configContext.setServiceName(temp.getServiceName());
        configContext.setConfigType(temp.getConfigType());
        configContext.setConfigKey(temp.getConfigKey());
        configContext.setKeyName(temp.getKeyName());
        configContext.setContent(temp.getContent());
        return configContext;
    }

    private ConfigQueryParam buildConfigQueryParam(ConfigQueryDTO dto) {
        ConfigQueryParam param = new ConfigQueryParam();
        param.setId(dto.getId());
        param.setServiceName(dto.getServiceName());
        return param;
    }
}
