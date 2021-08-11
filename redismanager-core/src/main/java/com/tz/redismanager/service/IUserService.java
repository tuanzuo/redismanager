package com.tz.redismanager.service;

import com.tz.redismanager.dao.domain.dto.UserAnalysisDTO;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.UserPageParam;
import com.tz.redismanager.domain.vo.UserVO;
import com.tz.redismanager.security.domain.AuthContext;

import java.util.List;

/**
 * <p>用户service接口</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 20:10
 **/
public interface IUserService {

    /**
     * 用户注册
     *
     * @param vo
     * @return
     */
    ApiResult<?> register(UserVO vo);

    /**
     * 在线人数
     *
     * @return
     */
    ApiResult<?> countOnline();

    /**
     * 当前用户信息
     *
     * @param authContext
     * @return
     */
    Object currentUser(AuthContext authContext);

    /**
     * 更新用户信息
     *
     * @param vo
     * @return
     */
    ApiResult<?> update(UserVO vo);

    /**
     * 批量修改用户状态
     *
     * @param ids
     * @param status
     * @param authContext
     * @return
     */
    ApiResult<?> updateStatus(List<Integer> ids, Integer status, AuthContext authContext);

    /**
     * 修改用户密码
     *
     * @param vo
     * @return
     */
    ApiResult<?> updatePwd(UserVO vo);

    /**
     * 重置用户密码
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> resetPwd(UserVO vo, AuthContext authContext);

    /**
     * 给用户分配角色
     *
     * @param vo
     * @param authContext
     * @return
     */
    ApiResult<?> grantRole(UserVO vo, AuthContext authContext);

    /**
     * 分页查询用户列表
     *
     * @param param
     * @return
     */
    ApiResult<?> queryList(UserPageParam param);

    /**
     * 查询用户分析页数据
     */
    List<UserAnalysisDTO> queryUserAnalysis();
}
