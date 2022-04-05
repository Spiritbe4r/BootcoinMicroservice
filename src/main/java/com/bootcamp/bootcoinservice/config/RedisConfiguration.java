package com.bootcamp.bootcoinservice.config;


import com.bootcamp.bootcoinservice.models.entities.BootCoin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {


  @Bean
  public ReactiveRedisTemplate<String, BootCoin> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {

    StringRedisSerializer keySerializer = new StringRedisSerializer();

    Jackson2JsonRedisSerializer<BootCoin> valueSerializer = new Jackson2JsonRedisSerializer<>(BootCoin.class);
    RedisSerializationContext.RedisSerializationContextBuilder<String, BootCoin> builder =
          RedisSerializationContext.newSerializationContext(keySerializer);

    RedisSerializationContext<String, BootCoin> context =
          builder.value(valueSerializer).build();

    return new ReactiveRedisTemplate<>(factory, context);
  }
}
