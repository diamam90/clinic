package com.diamam.doctorservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableScheduling
@EnableAsync
public class ScheduledConfig {

    @Bean
    public ThreadPoolTaskExecutor scheduledExecutor() {
        var corePool = Runtime.getRuntime().availableProcessors();
        var pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(corePool);
        pool.setMaxPoolSize(corePool * 2);
        pool.setKeepAliveSeconds(30);
        return pool;
    }
}
