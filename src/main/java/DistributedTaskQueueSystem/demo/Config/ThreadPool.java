package DistributedTaskQueueSystem.demo.Config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadPool {
    @Bean
    public ExecutorService jobExecutorService(){
        return Executors.newFixedThreadPool(5);
    }
}
