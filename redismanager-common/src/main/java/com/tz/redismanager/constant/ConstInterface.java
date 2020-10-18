package com.tz.redismanager.constant;

/**
 * 常量接口
 *
 * @Since:2019-08-23 22:27:52
 * @Version:1.1.0
 */
public interface ConstInterface {

    interface USER_STATUS {
        Integer ENABLE = 1;
        Integer DISABLE = 0;
    }

    interface ROLE_STATUS {
        Integer ENABLE = 1;
        Integer DISABLE = 0;
    }

    interface ROLE_CODE {
        String SUPER_ADMIN = "superadmin";
        String ADMIN = "admin";
        String TEST = "test";
        String DEVELOP = "develop";
    }

    interface IF_DEL {
        Integer YES = 1;
        Integer NO = 0;
    }

    //类型[1=单机,2=集群]
    interface TYPE {
        Integer SINGLE = 1;
        Integer CLUSTER = 2;
    }

    //来源[1添加,2修改]
    interface SOURCE {
        Integer ADD = 1;
        Integer UPDATE = 2;
    }

    //符号
    interface Symbol {
        String COLON = ":";
        String SPACE = " ";
        String COMMA = ",";
        String STAR = "*";
        String SPOT = ".";
        String UNDER_LINE = "_";
        String MIDDLE_LINE = "-";
        String MIDDLE_BRACKET_LEFT = "[";
        String MIDDLE_BRACKET_RIGHT = "]";
        String BRACKET_LEFT = "(";
        String BRACKET_RIGHT = ")";
    }

    interface Common {
        String ROOT_NODE_TITLE = "ROOT";
        String ROOT_NODE_KEY = "ROOT";
        int NO_EXPIRE = -1;
    }

    interface Cacher {
        String REDIS_CONFIG_CACHER = "redisConfigCacher";
        String ANALYSIS_CACHER = "analysisCacher";
    }

    //缓存key
    interface CacheKey {
        //认证信息的key
        String USER_AUTH = "rm:user:auth:";
        //映射到认证信息的key
        String USER_TO_AUTH = "rm:user:to:auth:";
        //在线用户
        String USER_ONLINE = "rm:user:online:";
        //访问量汇总
        String VISIT_TOTAL_ALL = "rm:vist:total:all";
        String VISIT_TOTAL = "rm:vist:total:";
        //访问量明细
        String VISIT_DETAIL = "rm:vist:detail:";
        //用户访问量汇总
        String USER_VISIT_TOTAL = "rm:vist:user:total:";
        //用户访问量明细
        String USER_VISIT_DETAIL = "rm:vist:user:dtl:";
        //用户访问量排行明细
        String USER_VISIT_RANGE_DETAIL = "rm:vist:user:range:dtl:";
        //Redis连接配置排行明细
        String REDIS_CONFIG_VISIT_DETAIL = "rm:vist:rds:cfg:range:dtl:";
    }

    interface Auth {
        String AUTHORIZATION = "Authorization";
    }
}
