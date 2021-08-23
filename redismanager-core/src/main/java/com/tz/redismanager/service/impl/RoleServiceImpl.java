package com.tz.redismanager.service.impl;

import com.tz.redismanager.cacher.annotation.Cacheable;
import com.tz.redismanager.cacher.annotation.L1Cache;
import com.tz.redismanager.cacher.annotation.L2Cache;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.dto.RoleAnalysisDTO;
import com.tz.redismanager.dao.domain.po.RolePO;
import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.*;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * <p>角色Service</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-08-30 20:10
 **/
@Service
public class RoleServiceImpl implements IRoleService {

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
        PageResp<RoleResp> resp = this.buildPageResp(param, total);
        if (total <= 0) {
            return new ApiResult<>(ResultCode.SUCCESS, resp);
        }
        List<RolePO> list = rolePOMapper.selectPage(param.getName(), param.getCode(), param.getStatus(),
                param.getOffset(), param.getRows(), ConstInterface.IF_DEL.NO);
        this.addRoleResp(resp.getList(), list);
        return new ApiResult<>(ResultCode.SUCCESS, resp);
    }

    @Cacheable(name = "角色分析页缓存", key = ConstInterface.CacheKey.ANALYSIS_ROLE, l1Cache = @L1Cache(expireDuration = 60, expireUnit = TimeUnit.SECONDS), l2Cache = @L2Cache(expireDuration = 120, expireUnit = TimeUnit.SECONDS))
    @Override
    public List<RoleAnalysisDTO> queryRoleAnalysis() {
        return rolePOMapper.selectToAnalysis(ConstInterface.IF_DEL.NO);
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
        rolePO.setIfDel(ConstInterface.IF_DEL.NO);
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

    private PageResp<RoleResp> buildPageResp(RolePageParam param, Integer total) {
        PageResp<RoleResp> resp = new PageResp();
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
