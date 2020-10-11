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
        String COMMA = ",";
        String STAR = "*";
        String SPOT = ".";
        String UNDERLINE = "_";
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
    }

    //缓存key
    interface CacheKey {
        //认证信息的key
        String USER_AUTH = "rm:user:auth:";
        //映射到认证信息的key
        String USER_TO_AUTH = "rm:user:to:auth:";
    }

    interface Auth {
        String AUTHORIZATION = "Authorization";
    }
}
