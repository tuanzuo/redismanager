package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.dao.domain.po.RolePO;
import com.tz.redismanager.domain.vo.*;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IRoleService;
import com.tz.redismanager.security.AuthContext;
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
    public ApiResult<?> add(RoleVO vo, AuthContext authContext) {
        if (this.checkRoleCodeExist(vo)) {
            return new ApiResult<>(ResultCode.EXIST_ROLE_CODE);
        }
        RolePO rolePO = this.buildAddRole(vo, authContext);
        rolePOMapper.insertSelective(rolePO);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> update(RoleVO vo, AuthContext authContext) {
        RolePO roleTemp = rolePOMapper.selectByPrimaryKey(vo.getId());
        if (null == roleTemp || null == roleTemp.getId()) {
            return new ApiResult<>(ResultCode.QUERY_NULL);
        }
        if (!roleTemp.getCode().equals(vo.getCode()) && this.checkRoleCodeExist(vo)) {
            return new ApiResult<>(ResultCode.EXIST_ROLE_CODE);
        }
        RolePO rolePO = this.buildUpdateRole(vo, authContext);
        rolePOMapper.updateByPrimaryKeySelective(rolePO);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> updateStatus(List<Integer> ids, Integer status, AuthContext authContext) {
        rolePOMapper.batchUpdateStatus(ids, status, authContext.getUserName());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> queryList(RolePageParam param) {
        RolePO queryRole = this.buildQueryRole(param);
        Integer total = rolePOMapper.countRole(queryRole);
        RoleListResp resp = this.buildRoleListResp(param, total);
        if (total <= 0) {
            return new ApiResult<>(ResultCode.SUCCESS, resp);
        }
        List<RolePO> list = rolePOMapper.selectPage(param.getName(), param.getCode(), param.getStatus(), param.getOffset(), param.getRows());
        this.addRoleResp(resp.getList(), list);
        return new ApiResult<>(ResultCode.SUCCESS, resp);
    }

    private RolePO buildAddRole(RoleVO vo, AuthContext authContext) {
        RolePO rolePO = new RolePO();
        rolePO.setName(vo.getName());
        rolePO.setCode(vo.getCode());
        rolePO.setStatus(vo.getStatus());
        rolePO.setNote(vo.getNote());
        rolePO.setCreater(authContext.getUserName());
        rolePO.setCreateTime(new Date());
        rolePO.setUpdater(authContext.getUserName());
        rolePO.setUpdateTime(new Date());
        return rolePO;
    }

    private boolean checkRoleCodeExist(RoleVO vo) {
        RolePO queryRole = new RolePO();
        queryRole.setCode(vo.getCode());
        int countRole = rolePOMapper.countRole(queryRole);
        if (countRole > 0) {
            return true;
        }
        return false;
    }

    private RolePO buildUpdateRole(RoleVO vo, AuthContext authContext) {
        RolePO rolePO = new RolePO();
        rolePO.setId(vo.getId());
        rolePO.setName(vo.getName());
        rolePO.setCode(vo.getCode());
        rolePO.setStatus(vo.getStatus());
        rolePO.setNote(vo.getNote());
        rolePO.setUpdater(authContext.getUserName());
        rolePO.setUpdateTime(new Date());
        return rolePO;
    }

    private RolePO buildQueryRole(RolePageParam param) {
        RolePO queryRole = new RolePO();
        queryRole.setName(param.getName());
        queryRole.setCode(param.getCode());
        queryRole.setStatus(param.getStatus());
        queryRole.setIfDel(ConstInterface.IF_DEL.NO);
        return queryRole;
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
