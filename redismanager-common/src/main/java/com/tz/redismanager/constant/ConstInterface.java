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

    interface IS_SUPER_ADMIN {
        Integer YES = 1;
        Integer NO = 0;
    }

    interface IS_PUBLIC {
        Integer YES = 1;
        Integer NO = 0;
    }

    interface IF_DEL {
        Integer YES = 1;
        Integer NO = 0;
    }

    /**
     * 类型[1=单机,2=集群]
     */
    interface TYPE {
        Integer SINGLE = 1;
        Integer CLUSTER = 2;
    }

    /**
     * 来源[1添加,2修改]
     */
    interface SOURCE {
        Integer ADD = 1;
        Integer UPDATE = 2;
    }

    /**
     * 符号
     */
    interface Symbol {
        String COLON = ":";
        String SPACE = " ";
        String BLANK = "";
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

    /**
     * 公共
     */
    interface Common {
        String ROOT_NODE_TITLE = "ROOT";
        String ROOT_NODE_KEY = "ROOT";
        int NO_EXPIRE = -1;
    }

    /**
     * 缓存key
     */
    interface CacheKey {
        /**
         * 公共前缀key
         */
        String COMMON_PRE_KEY = "rm:";
        /**
         * redis连接信息的key
         */
        String REDIS_CONFIG = COMMON_PRE_KEY + "rds:conf";
        /**
         * 分析页的key
         */
        String ANALYSIS = COMMON_PRE_KEY + "analysis";
        /**
         * 当前用户的key
         */
        String CURRENT_USER = COMMON_PRE_KEY + "crt:usr";
        /**
         * 认证信息的key
         */
        String USER_AUTH = COMMON_PRE_KEY + "user:auth:";
        /**
         * 映射到认证信息的key
         */
        String USER_TO_AUTH = COMMON_PRE_KEY + "user:to:auth:";
        /**
         * 在线用户
         */
        String USER_ONLINE = COMMON_PRE_KEY + "user:online:";
        /**
         * 访问量汇总
         */
        String VISIT_TOTAL_ALL = COMMON_PRE_KEY + "vist:total:all";
        String VISIT_TOTAL = COMMON_PRE_KEY + "vist:total:";
        /**
         * 访问量明细
         */
        String VISIT_DETAIL = COMMON_PRE_KEY + "vist:detail:";
        /**
         * 用户访问量汇总
         */
        String USER_VISIT_TOTAL = COMMON_PRE_KEY + "vist:user:total:";
        /**
         * 用户访问量明细
         */
        String USER_VISIT_DETAIL = COMMON_PRE_KEY + "vist:user:dtl:";
        /**
         * 用户访问量排行明细
         */
        String USER_VISIT_RANK_DETAIL = COMMON_PRE_KEY + "vist:user:rank:dtl:";
        /**
         * Redis连接配置排行明细
         */
        String REDIS_CONFIG_VISIT_RANK_DETAIL = COMMON_PRE_KEY + "vist:rds:cfg:rank:dtl:";
        /**
         * 验证码key
         */
        String CAPTCHA_KEY = COMMON_PRE_KEY + "captcha:";
    }

    /**
     * 认证
     */
    interface Auth {
        String AUTHORIZATION = "Authorization";
    }

    /**
     * token类型
     */
    interface TokenType {
        String REDIS = "redis";
    }
}
