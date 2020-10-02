package com.tz.redismanager.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RolePOMapper;
import com.tz.redismanager.dao.mapper.UserPOMapper;
import com.tz.redismanager.dao.mapper.UserRoleRelationPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.UserPageParam;
import com.tz.redismanager.domain.po.RolePO;
import com.tz.redismanager.domain.po.UserPO;
import com.tz.redismanager.domain.po.UserRoleRelationPO;
import com.tz.redismanager.domain.vo.Pagination;
import com.tz.redismanager.domain.vo.UserListResp;
import com.tz.redismanager.domain.vo.UserResp;
import com.tz.redismanager.domain.vo.UserVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.IAuthCacheService;
import com.tz.redismanager.service.ICipherService;
import com.tz.redismanager.service.IUserService;
import com.tz.redismanager.token.TokenContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

/**
 * <p>用户Service</p>
 *
 * @version 1.3.0
 * @time 2020-08-30 20:10
 **/
@Service
public class UserServiceImpl implements IUserService {

    private static final String DEFAULT_PWD = "123456";

    private static List<String> noteList = Arrays.asList("世界那么大", "我想去看看", "生活不只是苟且", "还有诗和远方",
            "有人与我立黄昏", "有人问我粥可温", "有人与我捻熄灯", "有人共我书半生",
            "有人陪我夜已深", "有人与我把酒分", "有人拭我相思泪", "有人梦我与前尘", "有人陪我顾星辰", "有人醒我茶已冷");

    @Autowired
    private ICipherService cipherService;
    @Autowired
    private IAuthCacheService authCacheService;
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
        String encodePwd = cipherService.encodeUserInfoByMd5(userPO.getName(), userPO.getPwd());
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
    public ApiResult<?> currentUser(TokenContext tokenContext) {
        UserPO userPO = userPOMapper.selectByPrimaryKey(tokenContext.getUserId());
        userPO.setPwd(null);
        //需要返回这些数据“个人页-个人设置”页面才能正常显示出来
        JSONObject jsonObject = JSONArray.parseObject("{\"name\":\"admin\",\"avatar\":\"https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png\",\"userid\":\"00000001\",\"email\":\"antdesign@alipay.com\",\"signature\":\"海纳百川，有容乃大\",\"title\":\"交互专家\",\"group\":\"蚂蚁金服－某某某事业群－某某平台部－某某技术部－UED\",\"tags\":[{\"key\":\"0\",\"label\":\"很有想法的\"},{\"key\":\"1\",\"label\":\"专注设计\"},{\"key\":\"2\",\"label\":\"辣~\"},{\"key\":\"3\",\"label\":\"大长腿\"},{\"key\":\"4\",\"label\":\"川妹子\"},{\"key\":\"5\",\"label\":\"海纳百川\"}],\"notifyCount\":12,\"unreadCount\":11,\"country\":\"China\",\"geographic\":{\"province\":{\"label\":\"四川省\",\"key\":\"330000\"},\"city\":{\"label\":\"成都市\",\"key\":\"330100\"}},\"address\":\"玉林路66号\",\"phone\":\"0752-268888888\"}");
        //覆盖某些参数
        jsonObject.put("name", userPO.getName());
        jsonObject.put("note", userPO.getNote());
        return new ApiResult<>(ResultCode.SUCCESS, jsonObject);
    }

    @Override
    public ApiResult<?> updateInfo(UserVO vo) {
        UserPO userTemp = userPOMapper.selectByPrimaryKey(vo.getId());
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
    public ApiResult<?> updateStatus(List<Integer> ids, Integer status, TokenContext tokenContext) {
        userPOMapper.batchUpdateStatus(ids, status, tokenContext.getUserName());
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
    public ApiResult<?> resetPwd(UserVO vo, TokenContext tokenContext) {
        UserPO userTemp = userPOMapper.selectByPrimaryKey(vo.getId());
        String encodePwd = cipherService.encodeUserInfoByMd5(userTemp.getName(), DEFAULT_PWD);
        UserPO update = new UserPO();
        update.setId(userTemp.getId());
        update.setPwd(encodePwd);
        update.setUpdater(tokenContext.getUserName());
        update.setUpdateTime(new Date());
        userPOMapper.updateByPrimaryKeySelective(update);

        //重置密码后删除缓存auth数据
        authCacheService.delAuthInfo(userTemp.getName(), userTemp.getPwd());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public UserListResp queryList(UserPageParam param) {
        Integer total = userPOMapper.countUser(param.getName(), param.getStatus());
        UserListResp resp = this.buildUserListResp(param, total);
        if (total <= 0) {
            return resp;
        }
        List<UserPO> list = userPOMapper.selectPage(param.getName(), param.getStatus(), param.getOffset(), param.getRows());
        this.addUserResp(resp.getList(), list);
        return resp;
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
            userResps.add(userResp);
        });
    }

}
