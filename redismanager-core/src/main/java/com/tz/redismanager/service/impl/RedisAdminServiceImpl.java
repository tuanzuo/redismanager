package com.tz.redismanager.service.impl;

import com.google.common.collect.Lists;
import com.tz.redismanager.annotation.ConnectionId;
import com.tz.redismanager.annotation.MethodLog;
import com.tz.redismanager.annotation.SetRedisTemplate;
import com.tz.redismanager.constant.ConstInterface;
import com.tz.redismanager.dao.domain.po.RedisConfigPO;
import com.tz.redismanager.domain.ApiResult;
import com.tz.redismanager.domain.vo.*;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.ResultCode;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.exception.RmException;
import com.tz.redismanager.service.IRedisAdminService;
import com.tz.redismanager.service.IRedisConfigService;
import com.tz.redismanager.strategy.HandlerFactory;
import com.tz.redismanager.strategy.IHandler;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.CommonUtils;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.RedisContextUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RedisAdminServiceImpl implements IRedisAdminService {

    private static final Logger logger = TraceLoggerFactory.getLogger(RedisAdminServiceImpl.class);

    @Autowired
    private IRedisConfigService redisConfigService;

    @SetRedisTemplate(whenIsNullContinueExec = true)
    @Override
    public List<RedisTreeNode> searchKey(@ConnectionId String id, String key) {
        //Root树节点List
        List<RedisTreeNode> treeNodesForRoot = new ArrayList<>();
        //构建Root树节点
        RedisTreeNode root = this.buildRootTreeNode(id);
        treeNodesForRoot.add(root);
        if (ConstInterface.Symbol.STAR.equals(key.trim()) || StringUtils.isBlank(StringUtils.replace(key.trim(), ConstInterface.Symbol.STAR, ConstInterface.Symbol.BLANK))) {
            logger.error("[RedisAdmin] [searchKey] {查询key不能为*或者全部为*}");
            return treeNodesForRoot;
        }
        //查询key集合
        Set<String> keySet = this.getKeys(id, key);
        if (CollectionUtils.isEmpty(keySet)) {
            return treeNodesForRoot;
        }
        //设置root节点的title
        this.setRootTreeNodeTitle(root, keySet.size());
        //对key集合排序
        List<String> keyList = this.sortKeys(keySet);
        keySet = null;

        long start = System.currentTimeMillis();
        //生成树--1、将节点封装到Map中
        List<RedisTreeNode> treeNodes = new ArrayList<>();
        Map<String, RedisTreeNode> map = new HashMap<>();
        Set<String> strSet = new HashSet<>();
        this.buildTreeNode(keyList, treeNodes, map, strSet);
        //生成树--2、设置tree的children tree
        this.buildTreeNodeChildren(treeNodes, map, strSet);
        keyList = null;

        logger.info("wrapper keys to tree time:{}ms", (System.currentTimeMillis() - start));
        //将查询到的keys生成的树节点List设置为Root树节点的子节点
        this.setRootTreeNodeChildren(root, treeNodes);
        return treeNodesForRoot;
    }

    @SetRedisTemplate
    @Override
    public RedisValueResp searchKeyValue(RedisValueQueryVO vo) {
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
            return new RedisValueResp();
        }
        String keyType = dataType.code();
        if (StringUtils.isBlank(keyType)) {
            logger.info("[RedisAdmin] [searchKeyValue] {通过vo:{}查询不到key的类型}", JsonUtils.toJsonStr(vo));
            return new RedisValueResp();
        }
        IHandler handler = HandlerFactory.getHandler(StrategyTypeEnum.SEARCH_VALUE, HandlerTypeEnum.getEnumByType(keyType));
        Object value = handler.handle(vo);
        return this.buildValueResp(vo, redisTemplate, keyType, value);
    }

    private RedisValueResp buildValueResp(RedisValueQueryVO vo, RedisTemplate<String, Object> redisTemplate, String keyType, Object value) {
        RedisValueResp resp = new RedisValueResp();
        resp.setValue(value);
        resp.setKeyType(keyType);
        resp.setExpireTime(redisTemplate.getExpire(vo.getSearchKey()));
        resp.setPageNum(vo.getPageNum());
        resp.setPageSize(vo.getPageSize());
        Long totalSize = null;
        if (HandlerTypeEnum.LIST.getType().equals(keyType)) {
            totalSize = redisTemplate.opsForList().size(vo.getSearchKey());
        } else if (HandlerTypeEnum.ZSET.getType().equals(keyType)) {
            totalSize = redisTemplate.opsForZSet().size(vo.getSearchKey());
        }
        resp.setTotalSize(totalSize);
        resp.setStart(vo.getStart());
        resp.setEnd(vo.getEnd());
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

    @MethodLog
    @SetRedisTemplate
    @Override
    public ApiResult<?> delKeys(RedisKeyDelVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        redisTemplate.delete(Lists.newArrayList(vo.getKeys()));
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @MethodLog
    @SetRedisTemplate
    @Override
    public ApiResult<?> renameKey(RedisKeyUpdateVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        //过期时间
        Long expireTime = redisTemplate.getExpire(vo.getOldKey());
        redisTemplate.rename(vo.getOldKey(), vo.getKey());
        if (null != expireTime && expireTime > 0) {
            redisTemplate.expire(vo.getKey(), expireTime, TimeUnit.SECONDS);
        }
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @MethodLog
    @SetRedisTemplate
    @Override
    public ApiResult<?> setTtl(RedisKeyUpdateVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        if (ConstInterface.Common.NO_EXPIRE == vo.getExpireTime()) {
            redisTemplate.persist(vo.getKey());
        } else {
            redisTemplate.expire(vo.getKey(), vo.getExpireTime(), TimeUnit.SECONDS);
        }
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @MethodLog
    @SetRedisTemplate
    @Override
    public ApiResult<?> updateValue(RedisKeyUpdateVO vo) {
        IHandler handler = HandlerFactory.getHandler(StrategyTypeEnum.UPDATE_VALUE, HandlerTypeEnum.getEnumByType(vo.getKeyType()));
        handler.handle(vo);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    @MethodLog
    @SetRedisTemplate
    @Override
    public ApiResult<?> addKey(RedisKeyAddVO vo) {
        RedisTemplate<String, Object> redisTemplate = RedisContextUtils.getRedisTemplate();
        if (redisTemplate.hasKey(vo.getKey())) {
            logger.warn("[RedisAdmin] [addKey] {添加Key已存在:{}}", JsonUtils.toJsonStr(vo));
            return new ApiResult<>(ResultCode.REDIS_KEY_EXIST.getCode(), "key:" + vo.getKey() + "已经存在,不能添加!");
        }
        IHandler handler = HandlerFactory.getHandler(StrategyTypeEnum.ADD_VALUE, HandlerTypeEnum.getEnumByType(vo.getKeyType()));
        handler.handle(vo);
        return new ApiResult<>(ResultCode.SUCCESS);
    }

    /**
     * 设置root节点的title
     * @param root
     * @param keyCount
     */
    private void setRootTreeNodeTitle(RedisTreeNode root, Integer keyCount) {
        root.setTitle(root.getTitle() + ConstInterface.Symbol.BRACKET_LEFT + keyCount + ConstInterface.Symbol.BRACKET_RIGHT);
    }

    /**
     * 构建Root树节点
     * @param id
     * @return
     */
    private RedisTreeNode buildRootTreeNode(String id) {
        String rootNodeTitle = ConstInterface.Common.ROOT_NODE_TITLE;
        RedisConfigPO configPO = redisConfigService.query(id);
        if (null != configPO) {
            rootNodeTitle = configPO.getName();
        }
        RedisTreeNode root = new RedisTreeNode(rootNodeTitle, ConstInterface.Common.ROOT_NODE_KEY, false);
        root.setDisableCheckbox(true);
        root.setDisabled(true);
        root.setLeaf(true);
        return root;
    }

    /**
     * 设置root节点的children
     * @param root
     * @param treeNodes
     */
    private void setRootTreeNodeChildren(RedisTreeNode root, List<RedisTreeNode> treeNodes) {
        if (CollectionUtils.isNotEmpty(treeNodes)) {
            root.setChildren(treeNodes);
            root.setDisableCheckbox(false);
            root.setDisabled(false);
            root.setLeaf(false);
        }
    }

    /**
     * 得到key集合
     * @param id
     * @param key
     * @return
     */
    private Set<String> getKeys(String id, String key) {
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
        logger.info("get keys time:{}ms,keys count:{}", (System.currentTimeMillis() - start), keySet.size());
        return keySet;
    }

    /**
     * 对key集合排序
     * @param keySet
     * @return
     */
    private List<String> sortKeys(Set<String> keySet){
        long start = System.currentTimeMillis();
        List<String> keyList = keySet.stream().sorted().collect(Collectors.toList());
        logger.info("sort keys time:{}ms", (System.currentTimeMillis() - start));
        return keyList;
    }

    /**
     * 构建Tree节点
     * @param keyList
     * @param treeNodes
     * @param map
     * @param strSet
     */
    private void buildTreeNode(List<String> keyList, List<RedisTreeNode> treeNodes, Map<String, RedisTreeNode> map, Set<String> strSet) {
        keyList.forEach(temp -> {
            String[] strs = StringUtils.split(temp, ConstInterface.Symbol.COLON);
            if (ArrayUtils.isNotEmpty(strs) && strs.length > 1) {
                this.buildTreeNodeMap(map, strSet, temp, strs);
            } else {
                treeNodes.add(new RedisTreeNode(temp, temp, true));
            }
        });
    }

    /**
     * 构建Tree节点的map集合
     * @param map
     * @param strSet
     * @param temp
     * @param strs
     */
    private void buildTreeNodeMap(Map<String, RedisTreeNode> map, Set<String> strSet, String temp, String[] strs) {
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
                tempkeyBuilder = new StringBuilder(temp);
                isLeaf = true;
            } else {
                tempkeyBuilder.append(ConstInterface.Symbol.COLON);
            }
            String tempkey = tempkeyBuilder.toString();
            strSet.add(tempkey);
            map.put(tempkey, new RedisTreeNode(title, title, tempkey, isLeaf, preTitle.toString()));
            preTitle.append(str).append(ConstInterface.Symbol.COLON);
        }
    }

    /**
     * 构建Tree节点的Children
     * @param treeNodes
     * @param map
     * @param strSet
     */
    private void buildTreeNodeChildren(List<RedisTreeNode> treeNodes, Map<String, RedisTreeNode> map, Set<String> strSet) {
        strSet.forEach(temp -> {
            RedisTreeNode node = map.get(temp);
            //存在父节点
            if (null != node && StringUtils.isNotBlank(node.getPkey())) {
                this.setTreeNodeChildren(map, node);
            } else {
                treeNodes.add(node);
            }
        });
    }

    /**
     * 设置Tree节点的Children
     * @param map
     * @param node
     */
    private void setTreeNodeChildren(Map<String, RedisTreeNode> map, RedisTreeNode node) {
        RedisTreeNode parent = map.get(node.getPkey());
        parent.addChildren(node);
        //如果是叶子节点，则增加父节点,父父...节点中叶子结点的个数
        if (node.getIsLeaf()) {
            this.setParentTreeNodeInfo(map, parent);
        }
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

}
