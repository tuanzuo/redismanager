package com.tz.redismanager.service.impl;

import com.tz.redismanager.bean.vo.RedisConfigVO;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.service.IRedisConfigService;
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
        po.setId(UUIDUtils.generateId());
        po.setCreater("admin");
        po.setCreateTime(new Date());
        po.setUpdater("admin");
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.NO);
        redisConfigPOMapper.insertSelective(po);
    }

    @Override
    public void delete(String id) {
        RedisConfigPO po = new RedisConfigPO();
        po.setId(id);
        po.setUpdater("admin");
        po.setUpdateTime(new Date());
        po.setIfDel(ConstInterface.IF_DEL.YES);
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
    }

    @Override
    public void update(RedisConfigVO vo) {
        RedisConfigPO po = new RedisConfigPO();
        BeanUtils.copyProperties(vo, po);
        po.setUpdater("admin");
        po.setUpdateTime(new Date());
        redisConfigPOMapper.updateByPrimaryKeySelective(po);
    }
}
