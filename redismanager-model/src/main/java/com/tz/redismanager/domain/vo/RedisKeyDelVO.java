package com.tz.redismanager.domain.vo;

import com.tz.redismanager.annotation.ConnectionId;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * redis key删除VO
 *
 * @author tuanzuo
 * @Since:2019-08-23 22:32:47
 * @Version:1.1.0
 */
@Getter
@Setter
public class RedisKeyDelVO {
    @NotNull(message = "id不能为空")
    @ConnectionId
    private Long id;

    @NotEmpty(message = "keys不能为空")
    private String[] keys;

}
