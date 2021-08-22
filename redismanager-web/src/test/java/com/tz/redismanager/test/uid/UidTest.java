package com.tz.redismanager.test.uid;

import com.baidu.fsg.uid.UidGenerator;
import com.tz.redismanager.RedisManagerWebApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p></p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-22 23:17
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedisManagerWebApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UidTest {

    @Autowired
    private UidGenerator uidGenerator;

    @Test
    public void testUidGenerator(){
        for (int i = 0; i < 1000; i++) {
           System.err.println(uidGenerator.getUID());
           System.err.println(uidGenerator.parseUID(uidGenerator.getUID()));
        }
    }
}
