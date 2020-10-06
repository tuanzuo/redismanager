package com.tz.redismanager.service.impl;

import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.po.RolePO;
import com.tz.redismanager.domain.vo.*;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IRoleService;
import com.tz.redismanager.token.TokenContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * <p>角色Service</p>
 *
 * @version 1.4.0
 * @time 2020-08-30 20:10
 **/
@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private RolePOMapper rolePOMapper;

    @Override
    public ApiResult<?> add(RoleVO vo, TokenContext tokenContext) {
        RolePO rolePO = this.buildAddRole(vo, tokenContext);
        rolePOMapper.insertSelective(rolePO);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private RolePO buildAddRole(RoleVO vo, TokenContext tokenContext) {
        RolePO rolePO = new RolePO();
        rolePO.setName(vo.getName());
        rolePO.setCode(vo.getCode());
        rolePO.setStatus(vo.getStatus());
        rolePO.setNote(vo.getNote());
        rolePO.setCreater(tokenContext.getUserName());
        rolePO.setCreateTime(new Date());
        rolePO.setUpdater(tokenContext.getUserName());
        rolePO.setUpdateTime(new Date());
        return rolePO;
    }

    @Override
    public ApiResult<?> update(RoleVO vo, TokenContext tokenContext) {
        RolePO roleTemp = rolePOMapper.selectByPrimaryKey(vo.getId());
        if (null == roleTemp || null == roleTemp.getId()) {
            return new ApiResult<>(ResultCode.QUERY_NULL);
        }
        RolePO rolePO = this.buildUpdateRole(vo, tokenContext);
        rolePOMapper.updateByPrimaryKey(rolePO);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private RolePO buildUpdateRole(RoleVO vo, TokenContext tokenContext) {
        RolePO rolePO = new RolePO();
        rolePO.setId(vo.getId());
        rolePO.setName(vo.getName());
        rolePO.setCode(vo.getCode());
        rolePO.setStatus(vo.getStatus());
        rolePO.setNote(vo.getNote());
        rolePO.setUpdater(tokenContext.getUserName());
        rolePO.setUpdateTime(new Date());
        return rolePO;
    }

    @Override
    public ApiResult<?> updateStatus(List<Integer> ids, Integer status, TokenContext tokenContext) {
        rolePOMapper.batchUpdateStatus(ids, status, tokenContext.getUserName());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public RoleListResp queryList(RolePageParam param) {
        Integer total = rolePOMapper.countRole(param.getName(), param.getCode(), param.getStatus());
        RoleListResp resp = this.buildRoleListResp(param, total);
        if (total <= 0) {
            return resp;
        }
        List<RolePO> list = rolePOMapper.selectPage(param.getName(), param.getCode(), param.getStatus(), param.getOffset(), param.getRows());
        this.addRoleResp(resp.getList(), list);
        return resp;
    }

    private RoleListResp buildRoleListResp(RolePageParam param, Integer total) {
        RoleListResp resp = new RoleListResp();
        Pagination pagination = new Pagination();
        pagination.setTotal(total);
        pagination.setCurrent(param.getCurrentPage());
        pagination.setPageSize(param.getPageSize());
        resp.setPagination(pagination);
        List<RoleResp> userResps = new ArrayList<>();
        resp.setList(userResps);
        return resp;
    }

    private void addRoleResp(List<RoleResp> roleResps, List<RolePO> list) {
        list = Optional.ofNullable(list).orElse(new ArrayList<>());
        list.forEach(temp -> {
            RoleResp roleResp = new RoleResp();
            BeanUtils.copyProperties(temp, roleResp);
            roleResps.add(roleResp);
        });
    }

}
