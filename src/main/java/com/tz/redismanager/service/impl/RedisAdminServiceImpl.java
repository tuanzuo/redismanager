package com.tz.redismanager.service.impl;

import com.google.common.collect.Lists;
import com.tz.redismanager.annotation.ConnectionId;
import com.tz.redismanager.annotation.MethodExecTime;
import com.tz.redismanager.annotation.SetRedisTemplate;
import com.tz.redismanager.bean.ResultCode;
import com.tz.redismanager.bean.po.RedisConfigPO;
import com.tz.redismanager.bean.vo.*;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.service.IRedisAdminService;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.strategy.HandlerFactory;
import com.tz.redismanager.strategy.IHandler;
import com.tz.redismanager.util.CommonUtils;
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
    private IRedisContextService redisContextService;

    @SetRedisTemplate(whenIsNullContinueExec = true)
    @Override
    public List<RedisTreeNode> searchKey(@ConnectionId String id, String key) {
        String rootNodeTitle = ConstInterface.Common.ROOT_NODE_TITLE;
        RedisConfigPO configPO = redisContextService.getRedisConfigCache().get(id);
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
            throw new RmException(ResultCode.REDIS_TEMPLATE_ISNULL.getCode(), "RedisTemplate为空,查询失败!");
        }
        long start = System.currentTimeMillis();
        //查询到的keys生成树节点的List
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
        List<RedisTreeNode> treeNodes = new ArrayList<>();
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
                    RedisTreeNode node = map.get(temp);
                    if (StringUtils.isNotBlank(node.getPkey())) {
                        RedisTreeNode parent = map.get(node.getPkey());
                        parent.addChildren(node);
                        //如果是叶子节点，则增加父节点,父父...节点中叶子结点的个数
                        if (node.getIsLeaf()) {
                            this.setParentTreeNodeInfo(map, parent);
                        }
                    } else {
                        treeNodes.add(node);
                    }
                }
            });
        }
        keyList = null;

        logger.info("wrapper tree time:{}", (System.currentTimeMillis() - start));
        //将查询到的keys生成的树节点List设置为Root树节点的子节点
        if (CollectionUtils.isNotEmpty(treeNodes)) {
            root.setChildren(treeNodes);
            root.setDisableCheckbox(false);
            root.setDisabled(false);
            root.setLeaf(false);
        }
        return treeNodesForRoot;
    }

    /**
     * 设置节点的title信息(包含叶子节点的数量)
     *
     * @param map
     * @param parent
     */
    private void setParentTreeNodeInfo(Map<String, RedisTreeNode> map, RedisTreeNode parent) {
        parent.setAllChildrenCount(parent.getAllChildrenCount() + 1);
        parent.setTitle(parent.getTempTitle() + "(" + parent.getAllChildrenCount() + ")");
        if (StringUtils.isNotBlank(parent.getPkey())) {
            this.setParentTreeNodeInfo(map, map.get(parent.getPkey()));
        }
    }

    @SetRedisTemplate
    @Override
    public RedisValueResp searchKeyValue(RedisValueQueryVo vo) {
        RedisValueResp resp = new RedisValueResp();
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
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
        return resp;
    }

    /**
     * 重新设置keySerializer
     *
     * @param redisTemplate
     */
    private void reSetKeySerializer(RedisTemplate<String, Object> redisTemplate) {
        CommonUtils.reSetKeySerializer(redisTemplate);
    }

    @MethodExecTime
    @SetRedisTemplate
    @Override
    public void delKeys(RedisKeyDelVo vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        redisTemplate.delete(Lists.newArrayList(vo.getKeys()));
    }

    @MethodExecTime
    @SetRedisTemplate
    @Override
    public void renameKey(RedisKeyUpdateVo vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        redisTemplate.rename(vo.getOldKey(), vo.getKey());
    }

    @MethodExecTime
    @SetRedisTemplate
    @Override
    public void setTtl(RedisKeyUpdateVo vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        if (-1 == vo.getExpireTime()) {
            redisTemplate.persist(vo.getKey());
        } else {
            redisTemplate.expire(vo.getKey(), vo.getExpireTime(), TimeUnit.SECONDS);
        }
    }

    @MethodExecTime
    @SetRedisTemplate
    @Override
    public void updateValue(RedisKeyUpdateVo vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //过期时间
        Long expireTime = redisTemplate.getExpire(vo.getKey());
        if ("string".equals(vo.getKeyType())) {
            RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
            //1、先试用string序列化方式把value存入redis
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            this.setValueForStringType(vo.getKey(), vo.getStringValue(), redisTemplate, expireTime);
            if (StringRedisSerializer.class.equals(valueSerializer.getClass())) {
                logger.info("[RedisAdmin] [updateValue] {更新Key的Value完成:{}}", JsonUtils.toJsonStr(vo));
                return;
            }
            //2、再查询出value,最后使用redisTemplate本身的序列化方式把数据存入redis
            Object valueTemp = redisTemplate.opsForValue().get(vo.getKey());
            redisTemplate.setValueSerializer(valueSerializer);
            this.setValueForStringType(vo.getKey(), valueTemp, redisTemplate, expireTime);
        }
    }

    private void setValueForStringType(String key, Object value, RedisTemplate<String, Object> redisTemplate, Long expireTime) {
        if (null != expireTime && null != key && null != value) {
            if (-1 == expireTime) {
                redisTemplate.opsForValue().set(key, value);
            } else if (expireTime > Integer.MAX_VALUE) {
                redisTemplate.opsForValue().set(key, value);
                redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            } else if (expireTime > 0) {
                redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            }
        }
    }
}
