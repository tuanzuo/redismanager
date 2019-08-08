package com.tz.redismanager.constant;

public interface ConstInterface {

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
    }

    interface Common {
        String ROOT_NODE_TITLE = "ROOT";
        String ROOT_NODE_KEY = "ROOT";
    }

    interface Cacher {
        String REDIS_CONFIG_CACHER = "redisConfigCacher";
    }
}
