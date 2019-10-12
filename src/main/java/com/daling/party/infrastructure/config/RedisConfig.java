package com.daling.party.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.List;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

@Configuration
@Slf4j
public class RedisConfig {

    @Value("${redis.cluster.host:}")
    private String redisClusterHost;

    @Value("${redis.host:localhost}")
    private String redisHost;

    @Value("${redis.port:6379}")
    private String redisPort;

    @Value("${redis.db:0}")
    private String redisDb;

    @Value("${redis.password:}")
    private String redisPassword;

    @Primary
    @Bean(name = "redisTemplate")
    @Scope(scopeName = SCOPE_PROTOTYPE)
    public RedisTemplate<Object, ?> redisTemplate(@Qualifier("rdRedisConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Primary
    @Bean(name = "rdRedisConnectionFactory")
    public RedisConnectionFactory rdRedisConnectionFactory() {
        if (this.isCluster()) {
            log.info("redis配置为集群模式,host:{}", this.redisClusterHost);
            return getClusterConnectionFactory();
        } else {
            log.info("redis为非集群模式,host:{},port:{},db:{},password:{}", this.redisHost, this.redisPort, this.redisDb, this.redisPassword);
            return getConnectionFactory();
        }
    }

    public StringRedisSerializer stringRedisSerializer() {
        return new StringRedisSerializer();
    }

    /**
     * 判断是否是集群，优先选用集群模式
     */
    private boolean isCluster() {
        boolean isCluster = !"".equals(this.redisClusterHost);
        return isCluster;
    }

    private RedisConnectionFactory getConnectionFactory() {
        //因非集群用于测试环境，硬编码
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(200);
        poolConfig.setMaxTotal(1024);
        poolConfig.setMaxWaitMillis(10000);
        poolConfig.setTestOnBorrow(true);
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
        connectionFactory.setUsePool(true);
        connectionFactory.setHostName(this.redisHost);
        connectionFactory.setPort(Integer.parseInt(this.redisPort));
        connectionFactory.setDatabase(Integer.parseInt(this.redisDb));
        connectionFactory.setPassword(this.redisPassword);
        return connectionFactory;
    }

    private RedisConnectionFactory getClusterConnectionFactory() {
        String[] split = this.redisClusterHost.split(",");
        List<String> nodes = Arrays.asList(split);
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        for (String node : nodes) {
            String[] split1 = node.split(":");
            clusterConfig.clusterNode(split1[0], Integer.valueOf(split1[1]));
        }
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(clusterConfig);

        connectionFactory.setTimeout(1000);//ms
        connectionFactory.setUsePool(true);
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(50);
        config.setMaxIdle(100);
        config.setMaxTotal(100);
        config.setBlockWhenExhausted(true);
        connectionFactory.setPoolConfig(config);
        return connectionFactory;
    }
}