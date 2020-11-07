package com.tz.redismanager.service.impl;

import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.param.RedisConfigPageParam;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.service.ICacheService;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.security.domain.AuthContext;
import com.tz.redismanager.util.RSAUtils;
import com.tz.redismanager.util.RsaException;
import com.tz.redismanager.util.UUIDUtils;
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
    private IRedisContextService redisContextService;
    @Autowired
    private ICacheService cacheService;
    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;

    @Override
    public List<RedisConfigPO> searchList(RedisConfigPageParam param) {
        return redisConfigPOMapper.selectPage(param.getSearchKey(), param.getOffset(), param.getRows());
    }

    @Override
    public ApiResult<?> add(RedisConfigVO vo, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        this.encryptPassWord(po);
        po.setId(UUIDUtils.generateId());
        po.setCreater(userName);
        po.setCreateTime(new Date());
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.NO);
        redisConfigPOMapper.insertSelective(po);
        //放入缓存
        cacheService.getCacher(ConstInterface.Cacher.REDIS_CONFIG_CACHER).put(po.getId(), po);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> delete(String id, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO po = new RedisConfigPO();
        po.setId(id);
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.YES);
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(id);
        cacheService.invalidateCache(ConstInterface.Cacher.REDIS_CONFIG_CACHER, id);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @Override
    public ApiResult<?> update(RedisConfigVO vo, AuthContext authContext) {
        String userName = authContext.getUserName();
        RedisConfigPO oldPO = (RedisConfigPO) cacheService.getCacher(ConstInterface.Cacher.REDIS_CONFIG_CACHER).get(vo.getId());
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        if (!StringUtils.equals(po.getPassword(), oldPO.getPassword())) {
            this.encryptPassWord(po);
        } else {
            po.setPassword(null);
        }
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(vo.getId());
        cacheService.invalidateCache(ConstInterface.Cacher.REDIS_CONFIG_CACHER, vo.getId());
        return new ApiResult<>(ResultCode.SUCCESS);
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
