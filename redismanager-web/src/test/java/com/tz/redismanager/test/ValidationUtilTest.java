package com.tz.redismanager.test;

import com.tz.redismanager.domain.vo.RedisConfigVO;
import com.tz.redismanager.util.ValidationUtils;
import com.tz.redismanager.validator.ValidGroup;
import org.junit.Test;

/**
 * Hibernate Validator验证工具测试
 * @version 1.0
 * @time 2019-08-16 22:59
 **/
public class ValidationUtilTest {

    @Test
    public void test() {
        RedisConfigVO vo = new RedisConfigVO();
        ValidationUtils.ValidResult validResult = ValidationUtils.validateBean(vo, ValidGroup.UpdateConnection.class);
        if (validResult.hasErrors()) {
            String errors = validResult.getErrors();
            System.out.println(errors);
        }
    }
}
