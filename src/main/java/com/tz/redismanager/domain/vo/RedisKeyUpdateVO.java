package com.tz.redismanager.domain.vo;

import com.tz.redismanager.annotation.ConnectionId;
import com.tz.redismanager.enm.HandlerTypeEnum;
import com.tz.redismanager.validator.ValidGroup;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.ScriptAssert;

/**
 * redis key修改VO
 *
 * @Since:2019-08-23 22:33:31
 * @Version:1.1.0
 */
@ScriptAssert.List({
        @ScriptAssert(lang = "javascript", groups = {ValidGroup.UpdateKeyValue.class},
                script = "_this.updateValueValidate(_this.keyType,_this.stringValue)", message = "value不能为空"),
        @ScriptAssert(lang = "javascript", groups = {ValidGroup.SetTTL.class},
                script = "_this.setTtlValidate(_this.expireTime)", message = "expireTime:过期时间为-1或者大于0")
})
public class RedisKeyUpdateVO {

    @NotEmpty(message = "id不能为空", groups = {ValidGroup.RenameKey.class, ValidGroup.SetTTL.class, ValidGroup.UpdateKeyValue.class})
    @ConnectionId
    private String id;
    @NotEmpty(message = "key不能为空", groups = {ValidGroup.RenameKey.class, ValidGroup.SetTTL.class, ValidGroup.UpdateKeyValue.class})
    private String key;
    @NotEmpty(message = "oldKey不能为空", groups = {ValidGroup.RenameKey.class})
    private String oldKey;
    /**
     * key类型：string,List,set,hash,zset
     */
    @NotEmpty(message = "key类型不能为空", groups = {ValidGroup.UpdateKeyValue.class})
    private String keyType;
    /**
     * 过期时间
     */
    private Long expireTime;
    /**
     * key对应的value(新值-修改后的值)
     */
    private String stringValue;
    /**
     * key对应的value(老值-修改前的值)
     */
    private String oldStringValue;

    public boolean updateValueValidate(String keyType, String stringValue) {
        if (HandlerTypeEnum.STRING.getType().equals(keyType)) {
            if (StringUtils.isBlank(stringValue)) {
                return false;
            }
        }
        return true;
    }

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

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getOldKey() {
        return oldKey;
    }

    public void setOldKey(String oldKey) {
        this.oldKey = oldKey;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getOldStringValue() {
        return oldStringValue;
    }

    public void setOldStringValue(String oldStringValue) {
        this.oldStringValue = oldStringValue;
    }
}
