package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.dao.mapper.UserRoleRelationPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.po.RolePO;
import com.tz.redismanager.domain.po.UserPO;
import com.tz.redismanager.domain.po.UserRoleRelationPO;
import com.tz.redismanager.domain.vo.UserVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Date;
import java.util.List;

/**
 * <p>用户Service</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 20:10
 **/
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private UserPOMapper userPOMapper;
    @Autowired
    private RolePOMapper rolePOMapper;
    @Autowired
    private UserRoleRelationPOMapper userRoleRelationPOMapper;

    @Override
    public ApiResult<?> register(UserVO vo) {
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(vo, userPO);
        userPO.setCreater(vo.getName());
        userPO.setCreateTime(new Date());
        userPO.setUpdater(vo.getName());
        userPO.setUpdateTime(new Date());
        userPO.setIfDel(ConstInterface.IF_DEL.NO);
        List<RolePO> roles = rolePOMapper.getAll();
        transactionTemplate.execute((transactionStatus) -> {
            userPOMapper.insertSelective(userPO);
            roles.forEach(temp -> {
                UserRoleRelationPO userRole = new UserRoleRelationPO();
                userRole.setUserId(userPO.getId());
                userRole.setRoleId(temp.getId());
                userRole.setCreater(vo.getName());
                userRole.setCreateTime(new Date());
                userRole.setUpdater(vo.getName());
                userRole.setUpdateTime(new Date());
                userRole.setIfDel(ConstInterface.IF_DEL.NO);
                userRoleRelationPOMapper.insertSelective(userRole);
            });
            return null;
        });
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> updateInfo(UserVO vo) {
        UserPO userTemp = userPOMapper.selectByName(vo.getOldName());
        UserPO userPO = new UserPO();
        userPO.setId(userTemp.getId());
        userPO.setName(vo.getName());
        userPO.setNote(vo.getNote());
        userPO.setUpdater(vo.getName());
        userPO.setUpdateTime(new Date());
        userPOMapper.updateByPrimaryKey(userPO);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> updatePwd(UserVO vo) {
        UserPO userTemp = userPOMapper.selectByName(vo.getName());
        int updateCont = userPOMapper.updateByPwd(userTemp.getId(), vo.getPwd(), vo.getOldPwd());
        if (updateCont != 1) {
            return new ApiResult<>(ResultCode.UPDATE_PWD_FAIL);
        }
        return new ApiResult<>(ResultCode.SUCCESS);
    }
}
