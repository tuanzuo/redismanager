package com.tz.redismanager.dao.domain.dto;

import com.tz.redismanager.dao.domain.po.PostmanConfigPO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;


/**
 * <p>postman配置DTO</p>
 *
 * @author tuanzuo
 * @version 1.7.1
 * @time 2021-11-13 20:59
 **/
@Getter
@Setter
@ToString
public class PostmanConfigDTO extends PostmanConfigPO {

    /**
     * 父id集合
     */
    private Set<Long> pids;
}
