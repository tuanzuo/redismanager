package com.tz.redismanager.service.impl;

import com.tz.redismanager.config.domain.param.ConfigPageParam;
import com.tz.redismanager.config.domain.po.ConfigPO;
import com.tz.redismanager.config.service.IConfigService;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.ConfigVO;
import com.tz.redismanager.domain.vo.PageResp;
import com.tz.redismanager.domain.vo.Pagination;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IConfigDecorator;
import com.tz.redismanager.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-04-22 20:54
 **/
@Service
public class ConfigDecoratorImpl implements IConfigDecorator {

    @Autowired(required = false)
    private IConfigService configService;

    @Override
    public ApiResult<?> queryList(ConfigPageParam param) {
        Integer total = configService.count(param);
        PageResp<ConfigPO> resp = this.buildPageResp(param, total);
        if (total <= 0) {
            return new ApiResult<>(ResultCode.SUCCESS, resp);
        }
        resp.setList(configService.queryList(param));
        return new ApiResult<>(ResultCode.SUCCESS, resp);
    }

    @Override
    public ApiResult<?> add(ConfigVO vo, AuthContext authContext) {
        configService.addConfig(this.buildAddPO(vo, authContext));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> update(ConfigVO vo, AuthContext authContext) {
        configService.updateConfig(this.buildUpdatePO(vo, authContext));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private PageResp<ConfigPO> buildPageResp(ConfigPageParam param, Integer total) {
        PageResp<ConfigPO> resp = new PageResp<>();
        Pagination pagination = new Pagination();
        pagination.setTotal(total);
        pagination.setCurrent(param.getCurrentPage());
        pagination.setPageSize(param.getPageSize());
        resp.setPagination(pagination);
        List<ConfigPO> list = new ArrayList<>();
        resp.setList(list);
        return resp;
    }

    private ConfigPO buildAddPO(ConfigVO vo, AuthContext authContext) {
        ConfigPO po = new ConfigPO();
        po.setType(vo.getType());
        po.setServiceName(vo.getServiceName());
        po.setKey(vo.getKey());
        po.setContent(JsonUtils.toJsonStr(vo.getContent()));
        po.setNote(vo.getNote());
        po.setCreater(authContext.getUserName());
        po.setCreateTime(new Date());
        po.setUpdater(authContext.getUserName());
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.NO);
        return po;
    }

    private ConfigPO buildUpdatePO(ConfigVO vo, AuthContext authContext) {
        ConfigPO po = new ConfigPO();
        po.setId(vo.getId());
        po.setType(vo.getType());
        po.setServiceName(vo.getServiceName());
        po.setKey(vo.getKey());
        po.setContent(JsonUtils.toJsonStr(vo.getContent()));
        po.setNote(vo.getNote());
        po.setUpdater(authContext.getUserName());
        po.setUpdateTime(new Date());
        return po;
    }

}
