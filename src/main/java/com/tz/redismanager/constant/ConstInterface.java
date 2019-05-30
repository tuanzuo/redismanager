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

    //符号
    interface Symbol {
        //冒号
        String COLON = ":";
        String COMMA = ",";
        String STAR = "*";
    }

    interface Common {
        String ROOT_NODE_TITLE = "ROOT";
        String ROOT_NODE_KEY = "ROOT";
    }
}
