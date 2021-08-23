package com.tz.redismanager.service;

import com.tz.redismanager.dao.domain.dto.RoleAnalysisDTO;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RolePageParam;
import com.tz.redismanager.domain.vo.RoleVO;
import com.tz.redismanager.security.domain.AuthContext;

import java.util.List;

/**
 * <p>角色service接口</p>
 *
 * @author tuanzuo
 * @version 1.4.0
 * @time 2020-08-30 20:10
 **/
public interface IRoleService {

    /**
     * 添加角色
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> add(RoleVO vo, AuthContext authContext);

    /**
     * 修改角色
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> update(RoleVO vo, AuthContext authContext);

    /**
     * 更新角色状态
     *
     * @param ids
     * @param status
     * @param authContext
     * @return
     */
    ApiResult<?> updateStatus(List<Integer> ids, Integer status, AuthContext authContext);

    /**
     * 分页查询角色列表
     *
     * @param param
     * @return
     */
    ApiResult<?> queryList(RolePageParam param);

    /**
     * 查询角色分析页数据
     */
    List<RoleAnalysisDTO> queryRoleAnalysis();
}
