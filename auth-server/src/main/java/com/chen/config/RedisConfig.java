package com.chen.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.jackson2.CoreJackson2Module;

@Configuration()
@RequiredArgsConstructor
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
public class RedisConfig  {

    private final Jackson2ObjectMapperBuilder builder;

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory connectionFactory){

        StringRedisSerializer stringRedisSerializer=new StringRedisSerializer();

        //创建ObjectMapper并添加配置
        ObjectMapper objectMapper=builder.createXmlMapper(false).build();

        //序列化所有字段
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        //该项必须配置
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        //添加security提供的Jackson Mixin
        objectMapper.registerModule(new CoreJackson2Module());


        //存入redis时序列化值的序列化器
        Jackson2JsonRedisSerializer<Object> valueSerializer=
                new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);


        RedisTemplate<Object,Object> template=new RedisTemplate<>();

        // 设置值序列化
        template.setValueSerializer(valueSerializer);
        // 设置hash格式数据值的序列化器
        template.setHashValueSerializer(valueSerializer);
        // 默认的Key序列化器为：JdkSerializationRedisSerializer
        template.setKeySerializer(stringRedisSerializer);
        // 设置字符串序列化器
        template.setStringSerializer(stringRedisSerializer);
        // 设置hash结构的key的序列化器
        template.setHashKeySerializer(stringRedisSerializer);

        //设置连接工厂
        template.setConnectionFactory(connectionFactory);

        return template;


    }



}
