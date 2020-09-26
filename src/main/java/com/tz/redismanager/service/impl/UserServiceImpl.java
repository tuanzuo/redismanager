package com.tz.redismanager.service.impl;

import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.dao.mapper.UserRoleRelationPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.po.RolePO;
import com.tz.redismanager.domain.po.UserPO;
import com.tz.redismanager.domain.po.UserRoleRelationPO;
import com.tz.redismanager.domain.vo.Pagination;
import com.tz.redismanager.domain.vo.UserListResp;
import com.tz.redismanager.domain.vo.UserResp;
import com.tz.redismanager.domain.vo.UserVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.DigestUtils;

import java.util.*;

/**
 * <p>用户Service</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 20:10
 **/
@Service
public class UserServiceImpl implements IUserService {

    private static final Integer PAGE_SIZE = 10;

    private static List<String> noteList = Arrays.asList("世界那么大，我想去看看", "生活不只是苟且，还有诗和远方", "有人与我立黄昏，有人问我粥可温", "有人与我捻熄灯，有人共我书半生",
            "有人陪我夜已深，有人与我把酒分", "有人拭我相思泪，有人梦我与前尘", "有人陪我顾星辰, 有人醒我茶已冷");

    @Value("${rd.encrypt.md5Salt}")
    private String md5Salt;

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
        String encodePwd = DigestUtils.md5DigestAsHex(String.format("%s_%s_%s", userPO.getName(), userPO.getPwd(), md5Salt).getBytes());
        userPO.setPwd(encodePwd);
        Collections.shuffle(noteList);
        userPO.setNote(noteList.get(0));
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
        String encodePwd = DigestUtils.md5DigestAsHex(String.format("%s_%s_%s", vo.getName(), vo.getPwd(), md5Salt).getBytes());
        String encodeOldPwd = DigestUtils.md5DigestAsHex(String.format("%s_%s_%s", vo.getName(), vo.getOldPwd(), md5Salt).getBytes());
        int updateCont = userPOMapper.updateByPwd(userTemp.getId(), encodePwd, encodeOldPwd);
        if (updateCont != 1) {
            return new ApiResult<>(ResultCode.UPDATE_PWD_FAIL);
        }
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public UserListResp queryList(String name, Integer currentPage, Integer pageSize) {
        UserListResp resp = new UserListResp();
        if (null == currentPage || currentPage <= 0) {
            currentPage = 1;
        }
        if (null == pageSize || pageSize <= 0) {
            pageSize = PAGE_SIZE;
        }
        int offset = (currentPage - 1) * pageSize;
        int rows = pageSize;

        Integer total = userPOMapper.countUser(name);
        Pagination pagination = new Pagination();
        pagination.setTotal(total);
        pagination.setCurrent(currentPage);
        pagination.setPageSize(pageSize);
        List<UserPO> list = userPOMapper.selectPage(name, offset, rows);
        list = Optional.ofNullable(list).orElse(new ArrayList<>());
        List<UserResp> userResps = new ArrayList<>();
        list.forEach(user -> {
            UserResp userResp = new UserResp();
            BeanUtils.copyProperties(user, userResp);
            userResps.add(userResp);
        });
        resp.setPagination(pagination);
        resp.setList(userResps);
        return resp;
    }
}