package com.tz.redismanager.service.impl;

import com.tz.redismanager.config.EncryptConfig;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.util.CommonUtils;
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

    private static final Integer PAGE_SIZE = 9;

    @Autowired
    private EncryptConfig encryptConfig;
    @Autowired
    private IRedisContextService redisContextService;
    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;

    @Override
    public List<RedisConfigPO> searchList(String searchKey, Integer pageNum, Integer pagesize) {
        if (StringUtils.isBlank(searchKey)) {
            searchKey = null;
        } else {
            searchKey = searchKey.trim();
        }
        if (null == pageNum || pageNum <= 0) {
            pageNum = 1;
        }
        if (null == pagesize || pagesize <= 0) {
            pagesize = PAGE_SIZE;
        }
        int offset = (pageNum - 1) * pagesize;
        int rows = pagesize;
        return redisConfigPOMapper.selectAll(searchKey, offset, rows);
    }

    @Override
    public void add(RedisConfigVO vo, String token) {
        String userName = CommonUtils.getUserNameByToken(token);
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
        redisContextService.getRedisConfigCache().put(po.getId(), po);
    }

    @Override
    public void delete(String id, String token) {
        String userName = CommonUtils.getUserNameByToken(token);
        RedisConfigPO po = new RedisConfigPO();
        po.setId(id);
        po.setUpdater(userName);
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.YES);
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
        //删除缓存中的RedisTemplate
        redisContextService.getRedisTemplateMap().remove(id);
        redisContextService.getRedisConfigCache().invalidate(id);
    }

    @Override
    public void update(RedisConfigVO vo, String token) {
        String userName = CommonUtils.getUserNameByToken(token);
        RedisConfigPO oldPO = redisContextService.getRedisConfigCache().get(vo.getId());
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
            po.setPassword(RSAUtils.rsaPublicEncrypt(po.getPassword(), encryptConfig.getPublicKey(), RSAUtils.CHARSET_UTF8));
        }
    }
}
