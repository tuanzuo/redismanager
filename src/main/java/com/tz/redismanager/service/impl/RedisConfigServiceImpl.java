package com.tz.redismanager.service.impl;

import com.tz.redismanager.bean.vo.RedisConfigVO;
import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.util.RSAUtil;
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
    private RedisConfigPOMapper redisConfigPOMapper;

    @Override
    public List<RedisConfigPO> searchList(String searchKey) {
        if (StringUtils.isBlank(searchKey)) {
            searchKey = null;
        } else {
            searchKey = searchKey.trim();
        }
        return redisConfigPOMapper.selectAll(searchKey);
    }

    @Override
    public void add(RedisConfigVO vo) {
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        this.encryptPassWord(po);
        po.setId(UUIDUtils.generateId());
        po.setCreater("admin");
        po.setCreateTime(new Date());
        po.setUpdater("admin");
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.NO);
        redisConfigPOMapper.insertSelective(po);
        //放入缓存
        redisContextService.getRedisConfigCache().put(po.getId(), po);
    }

    @Override
    public void delete(String id) {
        RedisConfigPO po = new RedisConfigPO();
        po.setId(id);
        po.setUpdater("admin");
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.YES);
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(id);
        redisContextService.getRedisConfigCache().invalidate(id);
    }

    @Override
    public void update(RedisConfigVO vo) {
        RedisConfigPO oldPO = redisContextService.getRedisConfigCache().get(vo.getId());
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        if (null != oldPO && null != oldPO.getPassword() && null != po.getPassword() && !oldPO.getPassword().equals(po.getPassword())) {
            this.encryptPassWord(po);
        } else {
            po.setPassword(null);
        }
        po.setUpdater("admin");
        po.setUpdateTime(new Date());
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(vo.getId());
        redisContextService.getRedisConfigCache().invalidate(vo.getId());
    }

    /**
     * 密码加密
     *
     * @param po
     * @throws RsaException
     */
    private void encryptPassWord(RedisConfigPO po) {
        if (StringUtils.isNotBlank(po.getPassword())) {
            po.setPassword(RSAUtil.rsaPublicEncrypt(po.getPassword(), encryptConfig.getPublicKey(), RSAUtil.CHARSET_UTF8));
        }
    }
}
