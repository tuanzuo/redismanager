package com.tz.redismanager.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.ParserConfig;
import com.tz.redismanager.RedisManagerApplication;
import com.tz.redismanager.bean.Person;
import com.tz.redismanager.config.FastJson2JsonRedisSerializer;
import com.tz.redismanager.util.JsonUtils;
import com.tz.redismanager.util.UUIDUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisTemplateTest {
	
    private static Logger logger = LoggerFactory.getLogger(RedisTemplateTest.class);

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

    private RedisTemplate<String, Object> myRedisTemplate;

    @Before
    public void testBefore() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setHost("127.0.0.1");

       /* RedisProperties.Cluster cluster = new RedisProperties.Cluster();
        String nodes = "192.168.1.32:7000,192.168.1.32:7001,192.168.1.32:7002,192.168.1.32:7003,192.168.1.32:7004,192.168.1.32:7005";
        cluster.setNodes(Arrays.asList(nodes.split(",")));
        cluster.setMaxRedirects(2);
        redisProperties.setCluster(cluster);*/

        MyRedisAutoConfiguration.RedisConnectionConfiguration redisConnectionConfiguration = new
                MyRedisAutoConfiguration.RedisConnectionConfiguration(redisProperties,null,null);
        JedisConnectionFactory redisConnectionFactory = redisConnectionConfiguration.redisConnectionFactory();
        redisConnectionFactory.afterPropertiesSet();

        RedisTemplate<String,Object> myRedisTemplate = new RedisTemplate<>();
        myRedisTemplate.setConnectionFactory(redisConnectionFactory);

        FastJson2JsonRedisSerializer<Object> fastJson2JsonRedisSerializer = new FastJson2JsonRedisSerializer<>(Object.class);
        //建议使用这种方式，小范围指定白名单
        ParserConfig.getGlobalInstance().addAccept("com.tz.redismanager.bean");
        //设置了白名单才能正常从缓存中序列化出来
        ParserConfig.getGlobalInstance().addAccept("com.tz.redis.bean");

        myRedisTemplate.setValueSerializer(fastJson2JsonRedisSerializer);
        myRedisTemplate.setHashValueSerializer(fastJson2JsonRedisSerializer);
        myRedisTemplate.setKeySerializer(new StringRedisSerializer());
        myRedisTemplate.setHashKeySerializer(new StringRedisSerializer());

        myRedisTemplate.afterPropertiesSet();

        this.myRedisTemplate = myRedisTemplate;
    }

	//StringRedisTemplate--保存字符串
	@Test
	public void testString() throws Exception {
        Person jsonperson = new Person("小明"+UUIDUtils.generateId(), 30);
        stringRedisTemplate.opsForValue().set("jsonvalue", JsonUtils.toJsonStr(jsonperson));

        /*Set<String> setList = myRedisTemplate.keys("*");
        setList.forEach(tep->{
            myRedisTemplate.delete(tep);
        });*/

        for (int i = 0; i < 100; i++) {
            String uuid = UUIDUtils.generateId();
            myRedisTemplate.opsForValue().set("a:" + uuid + ":userinfo:" + uuid + ":activityinfo:" + uuid + ":tian:" + uuid + ":tuanzuo:" + uuid, "value" + uuid);
        }
        for (int i = 0; i < 100; i++) {
            String uuid = UUIDUtils.generateId();
            myRedisTemplate.opsForValue().set("b:" + uuid + ":userinfo:" + uuid + ":activityinfo:" + uuid + ":tian:" + uuid + ":tuanzuo:" + uuid, "value" + uuid);
        }
        for (int i = 0; i < 100; i++) {
            String uuid = UUIDUtils.generateId();
            myRedisTemplate.opsForValue().set("c:" + uuid + ":userinfo:" + uuid + ":activityinfo:" + uuid + ":tian:" + uuid + ":tuanzuo:" + uuid, "value" + uuid);
        }
        for (int i = 0; i < 100; i++) {
            String uuid = UUIDUtils.generateId();
            myRedisTemplate.opsForValue().set("d:" + uuid + ":userinfo:" + uuid + ":activityinfo:" + uuid + ":tian:" + uuid + ":tuanzuo:" + uuid, "value" + uuid);
        }

        for (int i = 0; i < 100; i++) {
            String uuid = UUIDUtils.generateId();
            myRedisTemplate.opsForValue().set("tuanzuogood:" + uuid + ":userinfo:" + uuid + ":activityinfo:" + uuid + ":tian:" + uuid + ":tuanzuo:" + uuid, "value" + uuid);

            List<Person> list = new ArrayList<>();
            Person person = new Person("小明"+uuid, 30+i);
            Person chil = new Person("小明"+uuid+uuid, 30+i+i);
            person.setChild(chil);
            Person two1 = new Person("小天"+uuid, 20+i);
            Person two2 = new Person("小花"+uuid, 10+i);
            Person two3 = new Person("小章"+uuid, 30+i);

            list.add(person);
            list.add(two1);
            list.add(two2);
            list.add(two3);
            myRedisTemplate.opsForValue().set("tperson"+uuid, list);
        }
        Person person = new Person("小明", 30);
        myRedisTemplate.opsForValue().set("person", JSONArray.toJSON(person));
        System.err.println(myRedisTemplate.opsForValue().get("person"));
        myRedisTemplate.opsForHash().put("thash","thashkey1",new Person("小明hash1", 30));
        myRedisTemplate.opsForHash().put("thash","thashkey2",new Person("小明hash2", 30));

        myRedisTemplate.opsForList().leftPush("tlist",new Person("小明list1", 30));
        myRedisTemplate.opsForList().leftPush("tlist",new Person("小明list2", 30));

        myRedisTemplate.opsForSet().add("tset",new Person("小明set1", 1),new Person("小明set2", 30));
        myRedisTemplate.opsForSet().add("tset:tset",new Person("小明set1", 2),new Person("小明set2", 30));
        myRedisTemplate.opsForSet().add("tset:tset:tset",new Person("小明set1", 3),new Person("小明set2", 30));
        myRedisTemplate.opsForSet().add("tset:tset:tset:tset",new Person("小明set1", 4),new Person("小明set2", 30));
        myRedisTemplate.opsForSet().add("tset:tset:tset:tset:tset",new Person("小明set1", 5),new Person("小明set2", 30));
        myRedisTemplate.opsForSet().add("tset:tset:tset:tset:tset:001",new Person("小明set1", 6),new Person("小明set2", 30));
        myRedisTemplate.opsForSet().add("tset:tset:tset:tset:tset:002",new Person("小明set1", 7),new Person("小明set2", 30));

        myRedisTemplate.opsForZSet().add("tzset",new Person("小明zset1", 30),10);
        myRedisTemplate.opsForZSet().add("tzset",new Person("小明zset2", 30),20);

        Cursor<byte[]> cursor = myRedisTemplate.execute(new RedisCallback<Cursor<byte[]>>() {
            @Override
            public Cursor<byte[]> doInRedis(RedisConnection connection) throws DataAccessException {
                ScanOptions options = ScanOptions.scanOptions().match("*").count(1000).build();
                return connection.scan(options);
            }
        });
        Set<Object> binaryKeys = new HashSet<>();
        while (cursor.hasNext()) {
            binaryKeys.add(null != myRedisTemplate.getKeySerializer() ?
                    myRedisTemplate.getKeySerializer().deserialize(cursor.next()) : cursor.next());
        }
        System.err.println(binaryKeys);
    }

    @Test
    public void testT(){
        for (int i = 0; i < 15000; i++) {
            String uuid = UUIDUtils.generateId();
            myRedisTemplate.opsForValue().set("tuanzuogood:" + uuid + ":userinfo:" + uuid + ":activityinfo:" + uuid + ":tian:" + uuid + ":tuanzuo:" + uuid, "value" + uuid);

            List<Person> list = new ArrayList<>();
            Person person = new Person("小明"+uuid, 30+i);
            Person chil = new Person("小明"+uuid+uuid, 30+i+i);
            person.setChild(chil);
            Person two1 = new Person("小天"+uuid, 20+i);
            Person two2 = new Person("小花"+uuid, 10+i);
            Person two3 = new Person("小章"+uuid, 30+i);

            list.add(person);
            list.add(two1);
            list.add(two2);
            list.add(two3);
            myRedisTemplate.opsForValue().set("tperson"+uuid, list);
        }
    }
}