package com.tz.redismanager.service.impl;

import com.tz.redismanager.cacher.annotation.Cacher;
import com.tz.redismanager.cacher.annotation.CacherEvict;
import com.tz.redismanager.cacher.util.SpringUtils;
import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.util.RSAUtils;
import com.tz.redismanager.util.RsaException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RedisConfigServiceImpl implements IRedisConfigService {

    @Autowired
    private EncryptConfig encryptConfig;
    @Autowired
    private SpringUtils springUtils;
    @Autowired
    private IRedisContextService redisContextService;
    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;

    @Override
    public List<RedisConfigPO> searchList(RedisConfigPageParam param) {
        return redisConfigPOMapper.selectPage(param.getSearchKey(),
                param.getIsPublic(), param.getUserName(), param.getIsSuperAdmin(),
                param.getOffset(), param.getRows());
    }

    @Cacher(name = "redis连接配置信息缓存", key = ConstInterface.CacheKey.REDIS_CONFIG, var = "#id")
    @Override
    public RedisConfigPO query(String id) {
        return redisConfigPOMapper.selectByPrimaryKey(id);
    }

    @CacherEvict(name = "redis连接配置信息失效", key = ConstInterface.CacheKey.REDIS_CONFIG, var = "#id")
    @Override
    public void invalidateCache(String id) {

    }

    @Cacher(name = "redis连接配置信息缓存", key = ConstInterface.CacheKey.REDIS_CONFIG, var = "#vo.id")
    @Override
    public RedisConfigPO add(RedisConfigVO vo, AuthContext authContext) {
        RedisConfigPO po = this.buildAddRedisConfigPO(vo, authContext);
        redisConfigPOMapper.insertSelective(po);
        return po;
    }

    @Override
    public ApiResult<?> delete(String id, AuthContext authContext) {
        RedisConfigPO po = this.buildDelRedisConfigPO(id, authContext);
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(id);
        springUtils.getBean(IRedisConfigService.class).invalidateCache(id);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> update(RedisConfigVO vo, AuthContext authContext) {
        RedisConfigPO po = this.buildUpdateRedisConfigPO(vo, authContext);
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(vo.getId());
        springUtils.getBean(IRedisConfigService.class).invalidateCache(vo.getId());
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    private RedisConfigPO buildAddRedisConfigPO(RedisConfigVO vo, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        this.encryptPassWord(po);
        po.setId(vo.getId());
        po.setCreater(userName);
        po.setCreateTime(new Date());
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.NO);
        return po;
    }

    private RedisConfigPO buildDelRedisConfigPO(String id, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO po = new RedisConfigPO();
        po.setId(id);
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.YES);
        return po;
    }

    private RedisConfigPO buildUpdateRedisConfigPO(RedisConfigVO vo, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO oldPO = springUtils.getBean(IRedisConfigService.class).query(vo.getId());
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        if (!StringUtils.equals(po.getPassword(), oldPO.getPassword())) {
            this.encryptPassWord(po);
        } else {
            po.setPassword(null);
        }
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        return po;
    }

    /**
     * 密码加密
     *
     * @param po
     * @throws RsaException
     */
    private void encryptPassWord(RedisConfigPO po) {
        if (StringUtils.isNotBlank(po.getPassword())) {
            po.setPassword(RSAUtils.rsaPublicEncrypt(po.getPassword(), encryptConfig.getPublicKey(), RSAUtils.CHARSET_UTF8));
        }
    }
}
