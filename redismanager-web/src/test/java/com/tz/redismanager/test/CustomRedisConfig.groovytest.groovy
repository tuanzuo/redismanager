package com.tz.redismanager.test

import com.tz.redismanager.config.FastJson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

public class CustomRedisConfig extends Script{

    public void setRedisTemplateSerializer(){
        if (serializerCategory == 1) {
            FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
            customRedisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
            customRedisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);
            customRedisTemplate.setKeySerializer(new StringRedisSerializer());
            customRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        } else if (serializerCategory == 2) {
            customRedisTemplate.setValueSerializer(new StringRedisSerializer());
            customRedisTemplate.setHashValueSerializer(new StringRedisSerializer());
            customRedisTemplate.setKeySerializer(new StringRedisSerializer());
            customRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
            groovyRunDTO.setHitSerializerCategory(true)
        }
    }

    @Override
    Object run() {
        return setRedisTemplateSerializer();
    }
}