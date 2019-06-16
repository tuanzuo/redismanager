package com.tz.redismanager.service.impl;

import com.google.common.collect.Lists;
import com.tz.redismanager.annotation.SetRedisTemplate;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.bean.vo.*;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.service.IRedisAdminService;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisAdminServiceImpl implements IRedisAdminService {

    private static final Logger logger = LoggerFactory.getLogger(RedisAdminServiceImpl.class);

    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;

    @SetRedisTemplate
    @Override
    public List<RedisTreeNode> searchKey(String id, String key) {
        logger.info("[RedisAdmin] [searchKey] {正在通过id:{},key:{}查询keys}", id, key);
        String rootNodeTitle = ConstInterface.Common.ROOT_NODE_TITLE;
        RedisConfigPO configPO = redisConfigPOMapper.selectByPrimaryKey(id);
        if (null != configPO) {
            rootNodeTitle = configPO.getName();
        }

        //Root树节点List
        List<RedisTreeNode> treeNodesForRoot = new ArrayList<>();
        //构建Root树节点
        RedisTreeNode root = new RedisTreeNode(rootNodeTitle, ConstInterface.Common.ROOT_NODE_KEY, false);
        root.setDisableCheckbox(true);
        root.setDisabled(true);
        root.setLeaf(true);
        treeNodesForRoot.add(root);
        if (StringUtils.isBlank(key) || ConstInterface.Symbol.STAR.equals(key.trim())) {
            logger.error("[RedisAdmin] [searchKey] {查询key不能为空或者为*}");
            return treeNodesForRoot;
        }

        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        if (null == redisTemplate) {
            logger.error("[RedisAdmin] [searchKey] {id:{}查询不到redisTemplate}", id);
            return treeNodesForRoot;
        }
        //查询到的keys生成树节点的List
        List<RedisTreeNode> treeNodes = new ArrayList<>();
        Set<String> keySet = redisTemplate.keys(key.trim());
        if (CollectionUtils.isEmpty(keySet)) {
            //重新设置keySerializer
            this.reSetKeySerializer(redisTemplate);
            keySet = redisTemplate.keys(key.trim());
        }
        if (CollectionUtils.isEmpty(keySet)) {
            return treeNodesForRoot;
        }

        List<String> keyList = keySet.stream().sorted().collect(Collectors.toList());
        keySet = null;
        keyList.forEach(temp -> {
            String[] strs = StringUtils.split(temp, ConstInterface.Symbol.COLON);
            if (ArrayUtils.isNotEmpty(strs)) {
                boolean blankFlag = false;
                List<String> strList = new ArrayList<>();
                for (int i = 0; i < strs.length; i++) {
                    String str = strs[i];
                    strList.add(str);
                    if (StringUtils.isBlank(str)) {
                        blankFlag = true;
                        strList = null;
                        treeNodes.add(new RedisTreeNode(temp, temp, true));
                        break;
                    }
                }

                if (!blankFlag) {
                    int i = 1;
                    String title;
                    Boolean isLeaf = false;
                    RedisTreeNode preNode = null;
                    for (String str : strList) {
                        title = str;
                        if (i == strList.size()) {
                            title = temp;
                            isLeaf = true;
                        }
                        RedisTreeNode node = new RedisTreeNode(title, title, isLeaf);
                        if (i == 1) {
                            if (treeNodes.contains(node)) {
                                preNode = treeNodes.get(treeNodes.indexOf(node));
                            } else {
                                treeNodes.add(node);
                                preNode = node;
                            }
                        } else {
                            if (CollectionUtils.isNotEmpty(preNode.getChildren())) {
                                if (preNode.getChildren().contains(node)) {
                                    preNode = preNode.getChildren().get(preNode.getChildren().indexOf(node));
                                } else {
                                    preNode.getChildren().add(node);
                                    preNode = node;
                                }
                            } else {
                                preNode.addChildren(node);
                                preNode = node;
                            }
                        }

                        i++;
                    }
                }
            } else {
                treeNodes.add(new RedisTreeNode(temp, temp, true));
            }
        });
        //将查询到的keys生成的树节点List设置为Root树节点的子节点
        if (CollectionUtils.isNotEmpty(treeNodes)) {
            root.setChildren(treeNodes);
            root.setLeaf(false);
        }
        logger.info("[RedisAdmin] [searchKey] {通过id:{},key:{}查询keys生成TreeNode完成,result:{}}", id, key, JsonUtils.toJsonStr(treeNodesForRoot));
        return treeNodesForRoot;
    }

    @SetRedisTemplate
    @Override
    public RedisValueResp searchKeyValue(String id, RedisValueQueryVo vo) {
        RedisValueResp resp = new RedisValueResp();
        logger.info("[RedisAdmin] [searchKeyValue] {正在通过vo:{}查询key对应的value}", JsonUtils.toJsonStr(vo));
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        if (null == redisTemplate) {
            logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询不到redisTemplate}", vo.getId());
            return resp;
        }
        DataType dataType = null;
        try {
            dataType = redisTemplate.type(vo.getSearchKey());
            if (null == dataType || dataType == DataType.NONE) {
                //重新设置keySerializer
                this.reSetKeySerializer(redisTemplate);
                dataType = redisTemplate.type(vo.getSearchKey());
            }
        } catch (Exception e) {
            //重新设置keySerializer
            this.reSetKeySerializer(redisTemplate);
            dataType = redisTemplate.type(vo.getSearchKey());
        }
        if (null == dataType || dataType == DataType.NONE) {
            logger.info("[RedisAdmin] [searchKeyValue] {通过vo:{}查询不到key的类型}", JsonUtils.toJsonStr(vo));
            return resp;
        }
        String keyType = dataType.code();
        if (StringUtils.isBlank(keyType)) {
            logger.info("[RedisAdmin] [searchKeyValue] {通过vo:{}查询不到key的类型}", JsonUtils.toJsonStr(vo));
            return resp;
        }
        Object value = null;
        if (DataType.STRING.code().equals(keyType)) {
            try {
                value = redisTemplate.opsForValue().get(vo.getSearchKey());
                if (null == value) {
                    //重新设置keySerializer
                    this.reSetKeySerializer(redisTemplate);
                    value = redisTemplate.opsForValue().get(vo.getSearchKey());
                }
            } catch (Exception e) {
                logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
                logger.info("[RedisAdmin] [searchKeyValue] {从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
                redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
                value = redisTemplate.opsForValue().get(vo.getSearchKey());
            }
        }
        if (DataType.LIST.code().equals(keyType)) {
            try {
                //result = redisTemplate.opsForList().range(vo.getSearchKey(), 0, -1);
                value = redisTemplate.opsForList().range(vo.getSearchKey(), 0, 1000);
                if (null == value) {
                    //重新设置keySerializer
                    this.reSetKeySerializer(redisTemplate);
                    value = redisTemplate.opsForList().range(vo.getSearchKey(), 0, 1000);
                }
            } catch (Exception e) {
                logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
                logger.info("[RedisAdmin] [searchKeyValue] {从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
                redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
                value = redisTemplate.opsForList().range(vo.getSearchKey(), 0, 1000);
            }
        }
        if (DataType.HASH.code().equals(keyType)) {
            try {
                value = redisTemplate.opsForHash().entries(vo.getSearchKey());
                if (null == value) {
                    //重新设置keySerializer
                    this.reSetKeySerializer(redisTemplate);
                    value = redisTemplate.opsForHash().entries(vo.getSearchKey());
                }
            } catch (Exception e) {
                logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
                logger.info("[RedisAdmin] [searchKeyValue] {从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
                logger.info("[RedisAdmin] [searchKeyValue] {从{}切换到StringRedisSerializer处理}", redisTemplate.getHashValueSerializer().getClass().getSimpleName());
                redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
                redisTemplate.setHashValueSerializer(redisTemplate.getStringSerializer());
                value = redisTemplate.opsForHash().entries(vo.getSearchKey());
            }
        }
        if (DataType.SET.code().equals(keyType)) {
            try {
                value = redisTemplate.opsForSet().members(vo.getSearchKey());
                if (null == value) {
                    //重新设置keySerializer
                    this.reSetKeySerializer(redisTemplate);
                    value = redisTemplate.opsForSet().members(vo.getSearchKey());
                }
            } catch (Exception e) {
                logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
                logger.info("[RedisAdmin] [searchKeyValue] {从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
                redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
                value = redisTemplate.opsForSet().members(vo.getSearchKey());
            }
        }
        if (DataType.ZSET.code().equals(keyType)) {
            try {
                //result = redisTemplate.opsForZSet().rangeByScoreWithScores(vo.getSearchKey(), Double.MIN_VALUE, Double.MAX_VALUE);
                value = redisTemplate.opsForZSet().rangeByScoreWithScores(vo.getSearchKey(), Double.MIN_VALUE, Double.MAX_VALUE, 0, 1000);
                if (null == value) {
                    //重新设置keySerializer
                    this.reSetKeySerializer(redisTemplate);
                    value = redisTemplate.opsForZSet().rangeByScoreWithScores(vo.getSearchKey(), Double.MIN_VALUE, Double.MAX_VALUE, 0, 1000);
                }
            } catch (Exception e) {
                logger.error("[RedisAdmin] [searchKeyValue] {id:{}查询出错,message:{}}", vo.getId(), e.getMessage());
                logger.info("[RedisAdmin] [searchKeyValue] {从{}切换到StringRedisSerializer处理}", redisTemplate.getValueSerializer().getClass().getSimpleName());
                redisTemplate.setValueSerializer(redisTemplate.getStringSerializer());
                value = redisTemplate.opsForZSet().rangeByScoreWithScores(vo.getSearchKey(), Double.MIN_VALUE, Double.MAX_VALUE, 0, 1000);
            }
        }
        resp.setKeyType(keyType);
        resp.setExpireTime(redisTemplate.getExpire(vo.getSearchKey()));
        resp.setValue(value);
        logger.info("[RedisAdmin] [searchKeyValue] {通过vo:{}查询key对应的value完成,resp:{}}", JsonUtils.toJsonStr(vo), JsonUtils.toJsonStr(resp));
        return resp;
    }

    /**
     * 重新设置keySerializer
     * @param redisTemplate
     */
    private void reSetKeySerializer(RedisTemplate<String, Object> redisTemplate) {
        RedisSerializer keySerializer = redisTemplate.getKeySerializer();
        if (null != keySerializer) {
            if (keySerializer.getClass().getName().equals(StringRedisSerializer.class.getName())) {
                redisTemplate.setKeySerializer(redisTemplate.getDefaultSerializer());
            } else {
                redisTemplate.setKeySerializer(redisTemplate.getStringSerializer());
            }
            logger.info("[RedisAdmin] [reSetKeySerializer] {keySerializer从{}切换为:{}再查询}", keySerializer.getClass().getSimpleName(), redisTemplate.getKeySerializer().getClass().getSimpleName());
        }
    }

    @SetRedisTemplate
    @Override
    public void delKeys(String id, RedisKeyDelVo vo) {
        logger.info("[RedisAdmin] [delKeys] {正在删除id:{}下的keys:{}}", id, JsonUtils.toJsonStr(vo.getKeys()));
        if (ArrayUtils.isEmpty(vo.getKeys())) {
            logger.error("[RedisAdmin] [delKeys] {keys为空}", vo.getKeys());
            return;
        }
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        if (null == redisTemplate) {
            logger.error("[RedisAdmin] [delKeys] {id:{}查询不到redisTemplate}", vo.getId());
            return;
        }
        redisTemplate.delete(Lists.newArrayList(vo.getKeys()));
        logger.info("[RedisAdmin] [delKeys] {删除id:{}下的keys:{}完成}", id, JsonUtils.toJsonStr(vo.getKeys()));
    }

    @SetRedisTemplate
    @Override
    public void setTtl(String id, RedisKeyUpdateVo vo) {
        logger.info("[RedisAdmin] [setTtl] {正在设置TTL id:{}下的vo:{}}", id, JsonUtils.toJsonStr(vo));
        if (StringUtils.isBlank(vo.getKey())) {
            logger.error("[RedisAdmin] [setTtl] {key为空}");
            return;
        }
        if (null == vo.getExpireTime()) {
            logger.error("[RedisAdmin] [setTtl] {expireTime为空}");
            return;
        }
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        if (null == redisTemplate) {
            logger.error("[RedisAdmin] [setTtl] {id:{}查询不到redisTemplate}", vo.getId());
            return;
        }
        redisTemplate.expire(vo.getKey(),vo.getExpireTime(), TimeUnit.SECONDS);
        logger.info("[RedisAdmin] [setTtl] {设置TTL完成 id:{}下的vo:{}}", id, JsonUtils.toJsonStr(vo));
    }
}
