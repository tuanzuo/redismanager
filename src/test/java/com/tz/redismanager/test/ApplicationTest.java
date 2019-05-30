package com.tz.redismanager.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.ParserConfig;
import com.tz.redismanager.RedisManagerApplication;
import com.tz.redismanager.bean.Person;
import com.tz.redismanager.config.FastJson2JsonRedisSerializer;
import groovy.lang.*;
import groovy.util.GroovyScriptEngine;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisManagerApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ApplicationTest {
	
    private static Logger logger = LoggerFactory.getLogger(ApplicationTest.class);

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

    private RedisTemplate<String, Object> myRedisTemplate;

    @Before
    public void testBefore() throws Exception {
        RedisProperties redisProperties = new RedisProperties();
        RedisProperties.Cluster cluster = new RedisProperties.Cluster();
        String nodes = "192.168.1.32:7000,192.168.1.32:7001,192.168.1.32:7002,192.168.1.32:7003,192.168.1.32:7004,192.168.1.32:7005";
        cluster.setNodes(Arrays.asList(nodes.split(",")));
        cluster.setMaxRedirects(2);
        redisProperties.setCluster(cluster);
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
        myRedisTemplate.opsForValue().set("tuozuogood003","tuozuogood003");
		stringRedisTemplate.opsForValue().set("tuozuogood001", "111");
		System.err.println(stringRedisTemplate.opsForValue().get("aaa"));
        Person person = new Person("小明", 30);
        myRedisTemplate.opsForValue().set("tuozuogood002", JSONArray.toJSON(person));
        System.err.println(myRedisTemplate.opsForValue().get("person"));
    }

    @Test
    public void testGroovy(){
        try {
            Person person = new Person("tuanzuo", 30);
            GroovyScriptEngine groovyScriptEngine = new GroovyScriptEngine("src/test/java/com/tz/redismanager/test");
            Class scriptClass = groovyScriptEngine.loadScriptByName("hello2.groovy");
            GroovyObject scriptInstance = (GroovyObject)scriptClass.newInstance();
            Object ret = scriptInstance.invokeMethod("helloWithParam", new Object[]{person,"lxi"});
            System.out.println("testGroovy3:" + ret);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception e="+e.toString());
        }
    }
	
	@Test
	public void testGroovyOne(){
        Binding binding = new Binding();
        binding.setVariable("myRedisTemplate",this.myRedisTemplate);
        GroovyShell groovyShell = new GroovyShell(binding);
        String scriptContent=
                "import com.tz.redismanager.config.RedisObjectSerializer;\n" +
                "def setValue = myRedisTemplate.setValueSerializer(new RedisObjectSerializer());\n" +
                "def setHashValue = myRedisTemplate.setHashValueSerializer(new RedisObjectSerializer());\n" +
                "setValue;\n" +
                "setHashValue;";
        Script script = groovyShell.parse(scriptContent);
        System.err.println(script.run());
	}

    @Test
    public void testGroovyTwo(){
        Person person = new Person("小明", 30);
        myRedisTemplate.opsForValue().set("person", JSONArray.toJSON(person));

        Binding binding = new Binding();
        binding.setVariable("myRedisTemplate",this.myRedisTemplate);

        GroovyClassLoader groovyClassLoader = new GroovyClassLoader(this.getClass().getClassLoader());
        CompilerConfiguration compilerConfiguration= new CompilerConfiguration();
        compilerConfiguration.setSourceEncoding("utf-8");
        compilerConfiguration.setScriptBaseClass("TestRedisConf");

        GroovyShell groovyShell = new GroovyShell(groovyClassLoader,binding,compilerConfiguration);
        String scriptText=
                "import org.springframework.data.redis.serializer.StringRedisSerializer\n" +
                "import org.springframework.core.convert.converter.Converter;\n" +
                "import org.springframework.core.serializer.support.DeserializingConverter;\n" +
                "import org.springframework.core.serializer.support.SerializingConverter;\n" +
                "import org.springframework.data.redis.serializer.RedisSerializer;\n" +
                "import org.springframework.data.redis.serializer.SerializationException;\n" +
                "\n" +
                "public class TestRedisConf extends Script{\n" +
                "    \n" +
                "    public void setRedisTemplateSerializer(){\n" +
                "        myRedisTemplate.setValueSerializer(new MyRedisObjectSerializer());\n" +
                "        myRedisTemplate.setHashValueSerializer(new MyRedisObjectSerializer());\n" +
                "        myRedisTemplate.setKeySerializer(new StringRedisSerializer());\n" +
                "        myRedisTemplate.setHashKeySerializer(new StringRedisSerializer());\n" +
                "    }\n" +
                "    \n" +
                "    @Override\n" +
                "    Object run() {\n" +
                "        return setRedisTemplateSerializer();\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "public class MyRedisObjectSerializer implements RedisSerializer<Object> {\n" +
                "\n" +
                "    private Converter<Object, byte[]> serializer = new SerializingConverter();\n" +
                "    private Converter<byte[], Object> deserializer = new DeserializingConverter();\n" +
                "\n" +
                "    static final byte[] EMPTY_ARRAY = new byte[0];\n" +
                "\n" +
                "    public Object deserialize(byte[] bytes) {\n" +
                "        if (isEmpty(bytes)) {\n" +
                "            return null;\n" +
                "        }\n" +
                "\n" +
                "        try {\n" +
                "            return deserializer.convert(bytes);\n" +
                "        } catch (Exception ex) {\n" +
                "            throw new SerializationException(\"Cannot deserialize\", ex);\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public byte[] serialize(Object object) {\n" +
                "        if (object == null) {\n" +
                "            return EMPTY_ARRAY;\n" +
                "        }\n" +
                "\n" +
                "        try {\n" +
                "            return serializer.convert(object);\n" +
                "        } catch (Exception ex) {\n" +
                "            return EMPTY_ARRAY;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private boolean isEmpty(byte[] data) {\n" +
                "        return (data == null || data.length == 0);\n" +
                "    }\n" +
                "}";

        Script script = groovyShell.parse(scriptText);
        System.err.println(script.run());

        myRedisTemplate.opsForValue().set("person", JSONArray.toJSON(person));
    }

}