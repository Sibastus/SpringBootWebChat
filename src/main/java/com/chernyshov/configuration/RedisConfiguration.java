package com.chernyshov.configuration;


import com.chernyshov.entities.RedisMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port:6379}")
    private String redisPort;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() { //TODO use jedisStandaloneConfiguration
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(Integer.parseInt(redisPort));
        return jedisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, RedisMessage> redisTemplate() {
        RedisTemplate<String,RedisMessage> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
