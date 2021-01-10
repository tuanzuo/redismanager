package com.tz.redismanager.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tz.redismanager.cacher.annotation.Cacher;
import com.tz.redismanager.cacher.annotation.L1Cache;
import com.tz.redismanager.cacher.annotation.L2Cache;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.po.RolePO;
import com.tz.redismanager.dao.domain.po.UserPO;
import com.tz.redismanager.dao.domain.po.UserRoleRelationPO;
import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.dao.mapper.UserRoleRelationPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.UserPageParam;
import com.tz.redismanager.domain.vo.*;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>用户Service</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 20:10
 **/
@Service
public class UserServiceImpl implements IUserService {

    private static final String DEFAULT_PWD = "123456";
    private static final String ROLE_SUPER_ADMIN = "superadmin";

    private static List<String> noteList = Arrays.asList("世界那么大", "我想去看看", "生活不只是苟且", "还有诗和远方",
            "有人与我立黄昏", "有人问我粥可温", "有人与我捻熄灯", "有人共我书半生",
            "有人陪我夜已深", "有人与我把酒分", "有人拭我相思泪", "有人梦我与前尘", "有人陪我顾星辰", "有人醒我茶已冷");

    @Autowired
    private ICaptchaService captchaService;
    @Autowired
    private ICipherService cipherService;
    @Autowired
    private IAuthCacheService authCacheService;
    @Autowired
    private IStatisticService userStatisticsService;
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
        //校验验证码
        ApiResult<?> validCaptchaResult = captchaService.validCaptcha(new CaptchaVO(vo.getCaptchaKey(), vo.getCaptcha()));
        if (!ResultCode.SUCCESS.getCode().equals(validCaptchaResult.getCode())) {
            return new ApiResult<>(validCaptchaResult.getCode(), validCaptchaResult.getMsg());
        }

        //校验用户名是否存在
        UserPO queryUser = userPOMapper.selectByName(vo.getName());
        if (null != queryUser && null != queryUser.getId()) {
            return new ApiResult<>(ResultCode.USER_EXIST);
        }
        //构建注册对象
        UserPO userPO = this.buildRegisterUser(vo);
        //给用户设置角色
        List<RolePO> roles = rolePOMapper.getAll(ConstInterface.ROLE_STATUS.ENABLE);
        List<UserRoleRelationPO> userRoles = this.buildUserRoleRelations(vo, userPO, roles);
        transactionTemplate.execute((transactionStatus) -> {
            userPOMapper.insertSelective(userPO);
            if (CollectionUtils.isEmpty(userRoles)) {
                return null;
            }
            userRoles.forEach(temp -> {
                temp.setUserId(userPO.getId());
            });
            userRoleRelationPOMapper.insertBatch(userRoles);
            return null;
        });
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> countOnline() {
        Long count = userStatisticsService.countOnlineUser();
        return new ApiResult<>(ResultCode.SUCCESS, count);
    }

    @Cacher(name = "当前用户信息缓存", key = ConstInterface.CacheKey.CURRENT_USER, var = "#authContext.userId", l1Cache = @L1Cache(expireDuration = 2, expireUnit = TimeUnit.MINUTES), l2Cache = @L2Cache(expireDuration = 5, expireUnit = TimeUnit.MINUTES))
    @Override
    public Object currentUser(AuthContext authContext) {
        UserPO userPO = userPOMapper.selectByPrimaryKey(authContext.getUserId());
        userPO.setPwd(null);
        //需要返回这些数据“个人页-个人设置”页面才能正常显示出来
        JSONObject jsonObject = JSONArray.parseObject("{\"name\":\"admin\",\"avatar\":\"https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png\",\"userid\":\"00000001\",\"email\":\"antdesign@alipay.com\",\"signature\":\"海纳百川，有容乃大\",\"title\":\"交互专家\",\"group\":\"蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED\",\"tags\":[{\"key\":\"0\",\"label\":\"很有想法的\"},{\"key\":\"1\",\"label\":\"专注设计\"},{\"key\":\"2\",\"label\":\"辣~\"},{\"key\":\"3\",\"label\":\"大长腿\"},{\"key\":\"4\",\"label\":\"川妹子\"},{\"key\":\"5\",\"label\":\"海纳百川\"}],\"notifyCount\":12,\"unreadCount\":11,\"country\":\"China\",\"geographic\":{\"province\":{\"label\":\"四川省\",\"key\":\"330000\"},\"city\":{\"label\":\"成都市\",\"key\":\"330100\"}},\"address\":\"玉林路66号\",\"phone\":\"0752-268888888\"}");
        //覆盖某些参数
        jsonObject.put("name", userPO.getName());
        jsonObject.put("note", userPO.getNote());
        //未读消息条数-在线人数
        //jsonObject.put("unreadCount", userStatisticsService.countOnlineUser());
        jsonObject.put("unreadCount", 0);
        //右上角用户头像 v1.5.0
        jsonObject.put("avatar", "/img/BiazfanxmamNRoxxVxka.png");
        return jsonObject;
    }

    @Override
    public ApiResult<?> update(UserVO vo) {
        if (this.checkUserExist(vo)) {
            return new ApiResult<>(ResultCode.QUERY_NULL);
        }
        UserPO userPO = this.buildUpdateUser(vo);
        userPOMapper.updateByPrimaryKeySelective(userPO);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> updateStatus(List<Integer> ids, Integer status, AuthContext authContext) {
        userPOMapper.batchUpdateStatus(ids, status, authContext.getUserName());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> updatePwd(UserVO vo) {
        UserPO userTemp = userPOMapper.selectByPrimaryKey(vo.getId());
        String encodePwd = cipherService.encodeUserInfoByMd5(userTemp.getName(), vo.getPwd());
        String encodeOldPwd = cipherService.encodeUserInfoByMd5(userTemp.getName(), vo.getOldPwd());
        int updateCont = userPOMapper.updateByPwd(userTemp.getId(), encodePwd, encodeOldPwd);
        if (updateCont != 1) {
            return new ApiResult<>(ResultCode.UPDATE_PWD_FAIL);
        }

        //修改密码后删除缓存auth数据
        authCacheService.delAuthInfo(userTemp.getName(), userTemp.getPwd());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> resetPwd(UserVO vo, AuthContext authContext) {
        UserPO userTemp = userPOMapper.selectByPrimaryKey(vo.getId());
        UserPO update = this.buildResetPwdUser(authContext, vo, userTemp);
        userPOMapper.updateByPrimaryKeySelective(update);

        //重置密码后删除缓存auth数据
        authCacheService.delAuthInfo(userTemp.getName(), userTemp.getPwd());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> grantRole(UserVO vo, AuthContext authContext) {
        List<UserRoleRelationPO> userRoles = userRoleRelationPOMapper.selectByUserRoleRelation(vo.getId());
        //key:roleId,value:id
        Map<Integer, Integer> userRoleMap = userRoles.stream().collect(Collectors.toMap(UserRoleRelationPO::getRoleId, UserRoleRelationPO::getId));
        List<Integer> userRoleIdsToOld = userRoles.stream().map(UserRoleRelationPO::getRoleId).collect(Collectors.toList());
        List<Integer> userRoleIdsToNew = Optional.ofNullable(vo.getRoleIds()).orElse(new ArrayList<>());

        List<Integer> delUserRoleIds = (List<Integer>) CollectionUtils.removeAll(userRoleIdsToOld, userRoleIdsToNew);
        List<Integer> addUserRoleIds = (List<Integer>) CollectionUtils.removeAll(userRoleIdsToNew, userRoleIdsToOld);

        List<Integer> delIds = new ArrayList<>();
        delUserRoleIds.forEach(roleId -> {
            delIds.add(userRoleMap.get(roleId));
        });
        List<UserRoleRelationPO> addUserRoles = this.buildGrantUserRoleRelation(vo, addUserRoleIds, authContext);

        transactionTemplate.execute((transactionStatus) -> {
            if (CollectionUtils.isNotEmpty(delIds)) {
                userRoleRelationPOMapper.delByIds(delIds, authContext.getUserName(), new Date(), ConstInterface.IF_DEL.YES);
            }
            if (CollectionUtils.isNotEmpty(addUserRoles)) {
                userRoleRelationPOMapper.insertBatch(addUserRoles);
            }
            return null;
        });
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> queryList(UserPageParam param) {
        Integer total = userPOMapper.countUser(param.getName(), param.getStatus());
        UserListResp resp = this.buildUserListResp(param, total);
        if (total <= 0) {
            return new ApiResult<>(ResultCode.SUCCESS, resp);
        }
        List<UserPO> list = userPOMapper.selectPage(param.getName(), param.getStatus(), param.getOffset(), param.getRows());
        this.addUserResp(resp.getList(), list);
        List<RolePO> roles = rolePOMapper.getAll(null);
        this.setRoles(resp, roles);
        return new ApiResult<>(ResultCode.SUCCESS, resp);
    }

    private void setRoles(UserListResp resp, List<RolePO> roles) {
        roles = Optional.ofNullable(roles).orElse(new ArrayList<>());
        List<RoleVO> roleVOs = new ArrayList<>();
        roles.forEach(temp -> {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(temp, roleVO);
            roleVOs.add(roleVO);
        });
        resp.setRoles(roleVOs);
    }

    private UserPO buildRegisterUser(UserVO vo) {
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(vo, userPO);
        String encodePwd = cipherService.encodeUserInfoByMd5(userPO.getName(), userPO.getPwd());
        userPO.setPwd(encodePwd);
        Collections.shuffle(noteList);
        userPO.setNote(noteList.get(0));
        userPO.setCreater(vo.getName());
        userPO.setCreateTime(new Date());
        userPO.setUpdater(vo.getName());
        userPO.setUpdateTime(new Date());
        userPO.setIfDel(ConstInterface.IF_DEL.NO);
        return userPO;
    }

    private List<UserRoleRelationPO> buildUserRoleRelations(UserVO vo, UserPO userPO, List<RolePO> roles) {
        List<UserRoleRelationPO> userRoles = new ArrayList<>();
        roles.forEach(temp -> {
            if (ROLE_SUPER_ADMIN.equals(temp.getCode())) {
                return;
            }
            UserRoleRelationPO userRole = this.buildUserRoleRelation(vo, userPO, temp);
            userRoles.add(userRole);
        });
        return userRoles;
    }

    private UserRoleRelationPO buildUserRoleRelation(UserVO vo, UserPO userPO, RolePO temp) {
        UserRoleRelationPO userRole = new UserRoleRelationPO();
        userRole.setUserId(userPO.getId());
        userRole.setRoleId(temp.getId());
        userRole.setCreater(vo.getName());
        userRole.setCreateTime(new Date());
        userRole.setUpdater(vo.getName());
        userRole.setUpdateTime(new Date());
        userRole.setIfDel(ConstInterface.IF_DEL.NO);
        return userRole;
    }

    private boolean checkUserExist(UserVO vo) {
        UserPO userTemp = userPOMapper.selectByPrimaryKey(vo.getId());
        if (null == userTemp || null == userTemp.getId()) {
            return true;
        }
        return false;
    }

    private UserPO buildUpdateUser(UserVO vo) {
        UserPO userPO = new UserPO();
        userPO.setId(vo.getId());
        userPO.setName(vo.getName());
        userPO.setNote(vo.getNote());
        userPO.setUpdater(vo.getName());
        userPO.setUpdateTime(new Date());
        return userPO;
    }

    private UserPO buildResetPwdUser(AuthContext authContext, UserVO vo, UserPO userTemp) {
        String newPwd = StringUtils.isNotBlank(vo.getPwd()) ? vo.getPwd() : DEFAULT_PWD;
        String encodePwd = cipherService.encodeUserInfoByMd5(userTemp.getName(), newPwd);
        UserPO update = new UserPO();
        update.setId(userTemp.getId());
        update.setPwd(encodePwd);
        update.setUpdater(authContext.getUserName());
        update.setUpdateTime(new Date());
        return update;
    }

    private UserListResp buildUserListResp(UserPageParam param, Integer total) {
        UserListResp resp = new UserListResp();
        Pagination pagination = new Pagination();
        pagination.setTotal(total);
        pagination.setCurrent(param.getCurrentPage());
        pagination.setPageSize(param.getPageSize());
        resp.setPagination(pagination);
        List<UserResp> userResps = new ArrayList<>();
        resp.setList(userResps);
        return resp;
    }

    private void addUserResp(List<UserResp> userResps, List<UserPO> list) {
        list = Optional.ofNullable(list).orElse(new ArrayList<>());
        list.forEach(user -> {
            user.setPwd(null);
            UserResp userResp = new UserResp();
            BeanUtils.copyProperties(user, userResp);
            this.setUserRoles(userResp);
            userResps.add(userResp);
        });
    }

    private void setUserRoles(UserResp userResp) {
        List<RolePO> userRoles = userRoleRelationPOMapper.selectByUserRole(userResp.getId(), null);
        List<Integer> roleIds = userRoles.stream().map(RolePO::getId).collect(Collectors.toList());
        userResp.setRoleIds(roleIds);
    }

    private List<UserRoleRelationPO> buildGrantUserRoleRelation(UserVO vo, List<Integer> addUserRoleIds, AuthContext authContext) {
        List<UserRoleRelationPO> userRoles = new ArrayList<>();
        addUserRoleIds.forEach(roleId -> {
            UserRoleRelationPO roleRelation = new UserRoleRelationPO();
            roleRelation.setUserId(vo.getId());
            roleRelation.setRoleId(roleId);
            roleRelation.setCreater(authContext.getUserName());
            roleRelation.setCreateTime(new Date());
            roleRelation.setUpdater(authContext.getUserName());
            roleRelation.setUpdateTime(new Date());
            roleRelation.setIfDel(ConstInterface.IF_DEL.NO);
            userRoles.add(roleRelation);
        });
        return userRoles;
    }

}
