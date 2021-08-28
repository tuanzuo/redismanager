package com.tz.redismanager.test;

import com.tz.redismanager.RedisManagerWebApplication;
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
@SpringBootTest(classes = RedisManagerWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedisContextTest {
	
    private static Logger logger = LoggerFactory.getLogger(RedisContextTest.class);

    @Autowired
    private IRedisContextService redisContextService;

	@Test
	public void testInitContext() {
	    Long id = 1829600233578495L;
        redisContextService.initContext(id);
        RedisTemplate<String, Object> redisTemplate = redisContextService.getRedisTemplate(id);
        redisTemplate.opsForHash().put(id.toString(),"wen","wen001");

        id = 1829600233578496L;
        redisContextService.initContext(id);
        redisTemplate = redisContextService.getRedisTemplate(id);
        redisTemplate.opsForHash().put(id.toString(),"adminwen","wen002");
    }



}
