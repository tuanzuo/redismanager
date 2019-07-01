package com.tz.redismanager.service.impl;

import com.google.common.collect.Lists;
import com.tz.redismanager.annotation.ConnectionId;
import com.tz.redismanager.annotation.SetRedisTemplate;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.bean.vo.*;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.mapper.RedisConfigPOMapper;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.service.IRedisAdminService;
import com.tz.redismanager.strategy.HandlerFactory;
import com.tz.redismanager.strategy.IHandler;
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

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisAdminServiceImpl implements IRedisAdminService {

    private static final Logger logger = LoggerFactory.getLogger(RedisAdminServiceImpl.class);

    @Autowired
    private RedisConfigPOMapper redisConfigPOMapper;

    @SetRedisTemplate
    @Override
    public List<RedisTreeNode> searchKey(@ConnectionId String id, String key) {
        logger.info("[RedisAdmin] [searchKey] {正在通过id:{},key:{}查询keys}", id, key);
        long startAll = System.currentTimeMillis();
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
        long start = System.currentTimeMillis();
        //查询到的keys生成树节点的List
        List<RedisTreeNode> treeNodes = new ArrayList<>();
        Set<String> keySet = redisTemplate.keys(key.trim());
        if (CollectionUtils.isEmpty(keySet)) {
            //重新设置keySerializer
            this.reSetKeySerializer(redisTemplate);
            keySet = redisTemplate.keys(key.trim());
        }
        logger.info("keys time:{},keys count:{}", (System.currentTimeMillis() - start), keySet.size());
        if (CollectionUtils.isEmpty(keySet)) {
            return treeNodesForRoot;
        }
        start = System.currentTimeMillis();
        List<String> keyList = keySet.stream().sorted().collect(Collectors.toList());
        logger.info("sort time:{}", (System.currentTimeMillis() - start));
        start = System.currentTimeMillis();
        keySet = null;
        root.setTitle(root.getTitle() + "(" + keyList.size() + ")");

        //生成树--1、将节点封装到Map中
        Map<String, RedisTreeNode> map = new HashMap<>();
        Set<String> strSet = new HashSet<>();
        keyList.forEach(temp -> {
            String[] strs = StringUtils.split(temp, ConstInterface.Symbol.COLON);
            if (ArrayUtils.isNotEmpty(strs) && strs.length > 1) {
                String title = null;
                Boolean isLeaf = false;
                StringBuilder preTitle = new StringBuilder("");
                for (int i = 0; i < strs.length; i++) {
                    String str = strs[i];
                    title = str;
                    StringBuilder tempkeyBuilder = new StringBuilder();
                    tempkeyBuilder.append(preTitle.toString()).append(title);
                    if (i == strs.length - 1) {
                        title = temp;
                        isLeaf = true;
                    } else {
                        tempkeyBuilder.append(ConstInterface.Symbol.COLON);
                    }
                    String tempkey = tempkeyBuilder.toString();
                    strSet.add(tempkey);
                    map.put(tempkey, new RedisTreeNode(title, title, tempkey, isLeaf, preTitle.toString()));
                    preTitle.append(str).append(ConstInterface.Symbol.COLON);
                }
            } else {
                treeNodes.add(new RedisTreeNode(temp, temp, true));
            }
        });
        //生成树--2、设置tree的children tree
        if (CollectionUtils.isNotEmpty(strSet)) {
            strSet.forEach(temp -> {
                if (map.containsKey(temp)) {
                    if (StringUtils.isNotBlank(map.get(temp).getPkey())) {
                        RedisTreeNode parent = map.get(map.get(temp).getPkey());
                        parent.addChildren(map.get(temp));
                        parent.setTitle(parent.getTempTitle() + "(" + parent.getChildren().size() + ")");
                    } else {
                        treeNodes.add(map.get(temp));
                    }
                }
            });
        }
        keyList = null;

        logger.info("wrapper tree time:{}", (System.currentTimeMillis() - start));
        //将查询到的keys生成的树节点List设置为Root树节点的子节点
        if (CollectionUtils.isNotEmpty(treeNodes)) {
            root.setChildren(treeNodes);
            root.setLeaf(false);
        }
        //logger.info("[RedisAdmin] [searchKey] {通过id:{},key:{}查询keys生成TreeNode完成,result:{}}", id, key, JsonUtils.toJsonStr(treeNodesForRoot));
        logger.info("all time:{}", (System.currentTimeMillis() - startAll));
        return treeNodesForRoot;
    }

    @SetRedisTemplate
    @Override
    public RedisValueResp searchKeyValue(RedisValueQueryVo vo) {
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
        IHandler handler = HandlerFactory.getHandler(StrategyTypeEnum.SEARCH_VALUE, HandlerTypeEnum.getEnumByType(keyType));
        if (null != handler) {
            value = handler.handle(vo);
        }
        resp.setKeyType(keyType);
        resp.setExpireTime(redisTemplate.getExpire(vo.getSearchKey()));
        resp.setValue(value);
        logger.info("[RedisAdmin] [searchKeyValue] {通过vo:{}查询key对应的value完成}", JsonUtils.toJsonStr(vo));
        return resp;
    }

    /**
     * 重新设置keySerializer
     *
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
    public void delKeys(RedisKeyDelVo vo) {
        logger.info("[RedisAdmin] [delKeys] {正在删除keys:{}}", JsonUtils.toJsonStr(vo.getKeys()));
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
        logger.info("[RedisAdmin] [delKeys] {删除keys:{}完成}", JsonUtils.toJsonStr(vo.getKeys()));
    }

    @SetRedisTemplate
    @Override
    public void renameKey(RedisKeyUpdateVo vo) {
        logger.info("[RedisAdmin] [renameKey] {正在重命名key:{}}", JsonUtils.toJsonStr(vo));
        if (StringUtils.isAnyBlank(vo.getKey(), vo.getOldKey())) {
            logger.error("[RedisAdmin] [renameKey] {key或者oldKey为空}");
            return;
        }
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        if (null == redisTemplate) {
            logger.error("[RedisAdmin] [renameKey] {id:{}查询不到redisTemplate}", vo.getId());
            return;
        }
        redisTemplate.rename(vo.getOldKey(), vo.getKey());
        logger.info("[RedisAdmin] [renameKey] {重命名key完成:{}}", JsonUtils.toJsonStr(vo));
    }

    @SetRedisTemplate
    @Override
    public void setTtl(RedisKeyUpdateVo vo) {
        logger.info("[RedisAdmin] [setTtl] {正在设置TTL:{}}", JsonUtils.toJsonStr(vo));
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
        if (-1 == vo.getExpireTime()) {
            redisTemplate.persist(vo.getKey());
        } else {
            redisTemplate.expire(vo.getKey(), vo.getExpireTime(), TimeUnit.SECONDS);
        }
        logger.info("[RedisAdmin] [setTtl] {设置TTL完成:{}}", JsonUtils.toJsonStr(vo));
    }
}
