package com.tz.redismanager.strategy;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.InitializingBean;

/**
 * <p></p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-06-23 21:35
 **/
public abstract class AbstractHandler<T, R> implements InitializingBean, IHandler<T, R> {

    public abstract void putHandlerToMap(Object handlerType, IHandler handler);

    public abstract Object[] getHandlerType();

    public abstract IHandler getHandler(Object handlerType);

    @Override
    public void afterPropertiesSet() throws Exception {
        Object[] handlerTypes = getHandlerType();
        if (ArrayUtils.isNotEmpty(handlerTypes)) {
            for (Object handlerType : handlerTypes) {
                if (null != handlerType) {
                    putHandlerToMap(handlerType, this);
                }
            }
        }
    }
}
