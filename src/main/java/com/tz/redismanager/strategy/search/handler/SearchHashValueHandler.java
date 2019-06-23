package com.tz.redismanager.strategy.search.handler;

import com.tz.redismanager.bean.vo.RedisValueQueryVo;
import com.tz.redismanager.enm.SearchValueHandlerEnum;
import com.tz.redismanager.strategy.search.AbstractSearchHandler;
import org.springframework.stereotype.Service;

/**
 * <p></p>
 *
 * @author Administrator
 * @version 1.0
 * @time 2019-06-23 21:33
 **/
@Service
public class SearchHashValueHandler extends AbstractSearchHandler {

    @Override
    public Object[] getHandlerType() {
        return new Object[]{SearchValueHandlerEnum.HASH};
    }

    @Override
    public Object handle(RedisValueQueryVo redisValueQueryVo) {
        return null;
    }
}
