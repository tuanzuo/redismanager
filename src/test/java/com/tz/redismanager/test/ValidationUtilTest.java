package com.tz.redismanager.test;

import com.tz.redismanager.bean.vo.RedisConfigVO;
import com.tz.redismanager.util.ValidationUtil;
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
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(vo, ValidGroup.UpdateConnection.class);
        if (validResult.hasErrors()) {
            String errors = validResult.getErrors();
            System.out.println(errors);
        }
    }
}
