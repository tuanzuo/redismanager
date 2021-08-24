package com.tz.redismanager.uid.config;

import com.baidu.fsg.uid.UidGenerator;
import com.baidu.fsg.uid.impl.CachedUidGenerator;
import com.baidu.fsg.uid.impl.DefaultUidGenerator;
import com.baidu.fsg.uid.worker.DisposableWorkerIdAssigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * <p>Uid配置</p>
 *
 * @author tuanzuo
 * @version 1.7.0
 * @time 2021-08-22 22:57
 **/
@Configuration
@EnableConfigurationProperties({UidProperties.class})
public class UidConfig {

    private static final Logger logger = LoggerFactory.getLogger(UidConfig.class);

    @Autowired
    private UidProperties uidProperties;

    @Bean
    @ConditionalOnMissingBean
    public DisposableWorkerIdAssigner getDisposableWorkerIdAssigner() {
        return new DisposableWorkerIdAssigner();
    }

    @Lazy(value = false)
    @Bean
    @ConditionalOnProperty(name = "rm.uid.generator.strategy", havingValue = "default", matchIfMissing = true)
    public UidGenerator defaultUidGenerator(DisposableWorkerIdAssigner assigner) {
        DefaultUidGenerator uidGenerator =  new DefaultUidGenerator();
        this.setUidGeneratorInfo(assigner, uidGenerator);
        logger.info("[UidGenerator] build DefaultUidGenerator Finish");
        return uidGenerator;
    }

    @Bean
    @ConditionalOnProperty(name = "rm.uid.generator.strategy", havingValue = "cached", matchIfMissing = false)
    public UidGenerator cachedUidGenerator(DisposableWorkerIdAssigner assigner) {
        CachedUidGenerator uidGenerator =  new CachedUidGenerator();
        this.setUidGeneratorInfo(assigner, uidGenerator);
        logger.info("[UidGenerator] build CachedUidGenerator Finish");
        return uidGenerator;
    }

    private void setUidGeneratorInfo(DisposableWorkerIdAssigner assigner, DefaultUidGenerator uidGenerator) {
        uidGenerator.setWorkerIdAssigner(assigner);
        /** Specified bits & epoch as your demand. No specified the default value will be used */
        uidGenerator.setTimeBits(uidProperties.getTimeBits());
        uidGenerator.setWorkerBits(uidProperties.getWorkerBits());
        uidGenerator.setSeqBits(uidProperties.getSeqBits());
        uidGenerator.setEpochStr(uidProperties.getEpochStr());
    }
}
