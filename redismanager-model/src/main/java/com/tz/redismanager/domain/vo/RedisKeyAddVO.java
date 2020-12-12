package com.tz.redismanager.domain.vo;

import com.tz.redismanager.annotation.ConnectionId;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotEmpty;

/**
 * redis key添加VO
 *
 * @Since:2020-08-17 21:25:45
 * @Version:1.2.0
 */
@ScriptAssert.List({
        @ScriptAssert(lang = "javascript",
                script = "_this.setTtlValidate(_this.expireTime)", message = "expireTime:过期时间为-1或者大于0")
})
@Getter
@Setter
public class RedisKeyAddVO {

    @NotEmpty(message = "id不能为空")
    @ConnectionId
    private String id;
    @NotEmpty(message = "key不能为空")
    private String key;
    /**
     * key类型：string,List,set,hash,zset
     */
    @NotEmpty(message = "key类型不能为空")
    private String keyType;
    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * key对应的value
     */
    @NotEmpty(message = "key value不能为空")
    private String stringValue;

    public boolean setTtlValidate(Long expireTime) {
        if (null == expireTime) {
            return false;
        }
        if (-1 == expireTime || expireTime > 0) {
            return true;
        } else {
            return false;
        }
    }

}
