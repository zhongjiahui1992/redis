package com.zjh.redis.autoconfigure;

import com.zjh.redis.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableConfigurationProperties(JedisProperties.class)//开启属性注入,通过@autowired注入
@ConditionalOnClass(RedisClient.class)//判断这个类是否在classpath中存在
public class JedisAutoConfiguration {
    @Autowired
    private JedisProperties prop;

    @Bean(name="jedisPool")
    public JedisPool jedisPool() {
        System.out.println("Start Auto Configure JedisPool...");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(prop.getMaxTotal());
        config.setMaxIdle(prop.getMaxIdle());
        config.setMinIdle(prop.getMinIdle());
        config.setMaxWaitMillis(prop.getMaxWaitMillis());
        if(!StringUtils.isEmpty(prop.getPassword())){
            return new JedisPool(config, prop.getHost(), prop.getPort(), prop.getTimeout(),prop.getPassword(),prop.getDatabase());
        }else{
            return new JedisPool(config, prop.getHost(), prop.getPort(), prop.getTimeout());
        }
    }

    @Bean
    @ConditionalOnMissingBean(RedisClient.class)//容器中如果没有RedisClient这个类,那么自动配置这个RedisClient
    public RedisClient redisClient(@Qualifier("jedisPool")JedisPool pool) {
        RedisClient redisClient = new RedisClient();
        redisClient.setJedisPool(pool);
        System.out.println("End Auto Configure JedisPool = "+prop.getHost()+":"+prop.getPort());
        return redisClient;
    }
}
