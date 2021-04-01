package com.library.manage.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


@Slf4j
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    public Executor asyncPool() {
        if (log.isInfoEnabled()) {
            log.info("初始化{}线程池,参数:[corePoolSize:{},maxPoolSize:{},queueCapacity:{},keepAliveSeconds:{}]", "async", 10, 20, 40, 300);
        }
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池大小
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(20);
        //队列容量
        executor.setQueueCapacity(40);
        //活跃时间
        executor.setKeepAliveSeconds(300);
        //线程名字前缀
        executor.setThreadNamePrefix("%s-async");

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        if (log.isInfoEnabled()) {
            log.info("初始化 {}- 线程池完成=============", "async");
        }

        return executor;
    }
}
