package com.tz.redismanager.domain.vo;

import com.tz.redismanager.annotation.ConnectionId;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
