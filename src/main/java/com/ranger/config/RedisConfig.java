package com.ranger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


/**
 *   redis 链接池
 */
@Configuration
public class RedisConfig {
    @Value("${redis.url}")
    private String redisUrl;

    @Value("${redis.auth}")
    private String redisAuth;

    @Bean
    public JedisPool getJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        JedisPool pool = new JedisPool(config, redisUrl, 6379,3000,redisAuth);
        return pool;
    }

}