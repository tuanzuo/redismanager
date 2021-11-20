package com.tz.redismanager.test;

import com.tz.redismanager.RedisManagerWebApplication;
import com.tz.redismanager.domain.dto.GroovyRunDTO;
import com.tz.redismanager.service.IRedisContextService;
import com.tz.redismanager.util.RedisContextUtils;
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

    public static void main(String[] args) {
        String scode =
            "import com.tz.redismanager.config.FastJson2JsonRedisSerializer\n" +
            "import org.springframework.data.redis.serializer.StringRedisSerializer \n" +
            "public class CustomRedisConfig extends Script{\n" +
            "    public void setRedisTemplateSerializer(){\n" +
            "        if (serializerCategory == 1) {\n" +
            "            FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);\n" +
            "            customRedisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);\n" +
            "            customRedisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);\n" +
            "            customRedisTemplate.setKeySerializer(new StringRedisSerializer());\n" +
            "            customRedisTemplate.setHashKeySerializer(new StringRedisSerializer());\n" +
            "        } else if (serializerCategory == 2) {\n" +
            "            customRedisTemplate.setValueSerializer(new StringRedisSerializer());\n" +
            "            customRedisTemplate.setHashValueSerializer(new StringRedisSerializer());\n" +
            "            customRedisTemplate.setKeySerializer(new StringRedisSerializer());\n" +
            "            customRedisTemplate.setHashKeySerializer(new StringRedisSerializer());\n" +
            "            groovyRunDTO.setHitSerializerCategory(true)\n" +
            "        }\n" +
            "    }\n" +
            "    @Override\n" +
            "    Object run() {\n" +
            "        return setRedisTemplateSerializer();\n" +
            "    }\n" +
            "}";

        RedisTemplate redisTemplate = new RedisTemplate();
        GroovyRunDTO groovyRunDTO = RedisContextUtils.initRedisSerializerCommon(scode,redisTemplate,1);
        groovyRunDTO = RedisContextUtils.initRedisSerializerCommon(scode,redisTemplate,2);
        System.err.println(groovyRunDTO);
    }



}
