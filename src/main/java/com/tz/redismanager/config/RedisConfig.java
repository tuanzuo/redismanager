package com.tz.redismanager.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {
	
	/*@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		return new JedisConnectionFactory();
	}*/

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(redisConnectionFactory);

		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());

		//使用RedisObjectSerializer来序列化和反序列化redis的value值
		/*template.setValueSerializer(new RedisObjectSerializer());
		template.setHashValueSerializer(new RedisObjectSerializer());*/

		//使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
		/*template.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
		template.setHashValueSerializer(new Jackson2JsonRedisSerializer(Object.class));*/

		//使用Fastjson2JsonRedisSerializer来序列化和反序列化redis的value值
		template.setValueSerializer(new FastJson2JsonRedisSerializer(Object.class));
		template.setHashValueSerializer(new FastJson2JsonRedisSerializer(Object.class));

		template.afterPropertiesSet();
		return template;
	}
}
