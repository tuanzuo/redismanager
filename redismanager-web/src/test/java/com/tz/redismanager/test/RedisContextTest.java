package com.tz.redismanager.test;

import com.tz.redismanager.RedisManagerApplication;
import com.tz.redismanager.service.IRedisContextService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedisContextTest {
	
    private static Logger logger = LoggerFactory.getLogger(RedisContextTest.class);

    @Autowired
    private IRedisContextService redisContextService;

	@Test
	public void testInitContext() {
	    String id = "0c38bf1adbb6447288704c316f2a3bfc";
        redisContextService.initContext(id);
        RedisTemplate<String, Object> redisTemplate = redisContextService.getRedisTemplate(id);
        redisTemplate.opsForHash().put(id,"wen","wen001");

        id = "d850a65a64e64898918d49f01d019307";
        redisContextService.initContext(id);
        redisTemplate = redisContextService.getRedisTemplate(id);
        redisTemplate.opsForHash().put(id,"adminwen","wen002");
    }



}