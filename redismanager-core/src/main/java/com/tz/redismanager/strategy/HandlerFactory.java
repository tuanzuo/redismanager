package com.tz.redismanager.strategy;

import com.tz.redismanager.annotation.HandlerType;
import com.tz.redismanager.annotation.StrategyType;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.enm.StrategyTypeEnum;
import com.tz.redismanager.trace.TraceLoggerFactory;
import com.tz.redismanager.util.JsonUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理器Factory
 *
 * @author tuanzuo
 * @version 1.0
 * @time 2019-06-30 17:32
 **/
@Component
public class HandlerFactory implements InitializingBean {

    private static final Logger logger = TraceLoggerFactory.getLogger(HandlerFactory.class);

    private static Map<StrategyTypeEnum, Map<HandlerTypeEnum, IHandler>> strategyMap = new HashMap<>();

    @Autowired
    private List<IHandler> handlers;

    @Override
    public void afterPropertiesSet() throws Exception {
        handlers.forEach((handler) -> {
            Class clazz = handler.getClass();
            if (!clazz.isAnnotationPresent(StrategyType.class) || !clazz.isAnnotationPresent(HandlerType.class)) {
                return;
            }
            StrategyType strategyType = (StrategyType) clazz.getAnnotation(StrategyType.class);
            HandlerType handlerType = (HandlerType) clazz.getAnnotation(HandlerType.class);
            if (ArrayUtils.isEmpty(strategyType.value()) || ArrayUtils.isEmpty(handlerType.value())) {
                return;
            }
            for (StrategyTypeEnum strategyTypeEnum : strategyType.value()) {
                if (null == strategyTypeEnum) {
                    continue;
                }
                Map<HandlerTypeEnum, IHandler> handlerMap = null;
                if (strategyMap.containsKey(strategyTypeEnum)) {
                    handlerMap = strategyMap.get(strategyTypeEnum);
                } else {
                    handlerMap = new HashMap<>();
                }
                for (HandlerTypeEnum handlerTypeEnum : handlerType.value()) {
                    if (null == handlerTypeEnum) {
                        continue;
                    }
                    handlerMap.put(handlerTypeEnum, handler);
                    logger.info("[HandlerFactory] [{}] [{}] [初始化完成]", strategyTypeEnum, handlerTypeEnum);
                }
                strategyMap.put(strategyTypeEnum, handlerMap);
            }
        });
    }

    public static IHandler getHandler(StrategyTypeEnum strategyType, HandlerTypeEnum handlerType) {
        if (null == strategyType || null == handlerType) {
            logger.warn("[HandlerFactory] [getHandler] {strategyMap:{},strategyType:{},handlerType:{}不能为空}", strategyType, handlerType);
            return new EmptyHandler();
        }
        Map<HandlerTypeEnum, IHandler> handlerMap = strategyMap.get(strategyType);
        if (MapUtils.isEmpty(handlerMap) || !handlerMap.containsKey(handlerType)) {
            logger.warn("[HandlerFactory] [getHandler] {通过strategyMap:{},strategyType:{},handlerType:{}查询不到对应的handler!}", JsonUtils.toJsonStr(strategyMap), strategyType, handlerType);
            return new EmptyHandler();
        }
        return handlerMap.get(handlerType);
    }

    /**
     * 空Handler，减少调用处的判空和空指向
     */
    public static class EmptyHandler implements IHandler<Object, Object> {

        @Override
        public Object handle(Object o) {
            return new Object();
        }
    }
}
