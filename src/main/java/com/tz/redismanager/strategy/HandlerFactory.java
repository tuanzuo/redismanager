package com.tz.redismanager.strategy;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.annotation.StrategyType;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理器Factory
 *
 * @version 1.0
 * @time 2019-06-30 17:32
 **/
@Component
public class HandlerFactory implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(HandlerFactory.class);

    private static Map<StrategyTypeEnum, Map<HandlerTypeEnum, IHandler>> strategyMap = new HashMap<>();

    @Autowired
    private List<IHandler> handlers;

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("[HandlerFactory] {初始化HandlerFactory...}");
        handlers.forEach((handler) -> {
            Class clazz = handler.getClass();
            if (clazz.isAnnotationPresent(StrategyType.class) && clazz.isAnnotationPresent(HandlerType.class)) {
                StrategyType strategyType = (StrategyType) clazz.getAnnotation(StrategyType.class);
                HandlerType handlerType = (HandlerType) clazz.getAnnotation(HandlerType.class);
                if (ArrayUtils.isNotEmpty(strategyType.value()) && ArrayUtils.isNotEmpty(handlerType.value())) {
                    for (StrategyTypeEnum strategyTypeEnum : strategyType.value()) {
                        if (null != strategyTypeEnum) {
                            Map<HandlerTypeEnum, IHandler> handlerMap = null;
                            if (strategyMap.containsKey(strategyTypeEnum)) {
                                handlerMap = strategyMap.get(strategyTypeEnum);
                            } else {
                                handlerMap = new HashMap<>();
                            }
                            for (HandlerTypeEnum handlerTypeEnum : handlerType.value()) {
                                if (null != handlerTypeEnum) {
                                    handlerMap.put(handlerTypeEnum, handler);
                                }
                            }
                            strategyMap.put(strategyTypeEnum, handlerMap);
                        }
                    }
                }
            }
        });
        logger.info("[HandlerFactory] {初始化HandlerFactory完成.}");
    }

    public static IHandler getHandler(StrategyTypeEnum strategyType, HandlerTypeEnum handlerType) {
        if (MapUtils.isEmpty(strategyMap) || null == strategyType || null == handlerType) {
            logger.warn("[HandlerFactory] [getHandler] {strategyMap:{},strategyType:{},handlerType:{}不能为空}", JsonUtils.toJsonStr(strategyMap), strategyType, handlerType);
            return null;
        }
        if (strategyMap.containsKey(strategyType)) {
            Map<HandlerTypeEnum, IHandler> handlerMap = strategyMap.get(strategyType);
            if (MapUtils.isNotEmpty(handlerMap) && handlerMap.containsKey(handlerType)) {
                return handlerMap.get(handlerType);
            }
        }
        logger.warn("[HandlerFactory] [getHandler] {通过strategyMap:{},strategyType:{},handlerType:{}查询不到对应的handler!}", JsonUtils.toJsonStr(strategyMap), strategyType, handlerType);
        return null;
    }
}
