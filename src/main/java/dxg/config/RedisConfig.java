package dxg.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import sun.reflect.generics.visitor.Reifier;

/**
 *      redis 配置
 *
 * @author dingxigui
 * @date 2020/4/22
 */
@Configuration
public class RedisConfig {


  @Bean
  @ConditionalOnMissingBean(name = "redisTemplate")
  public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
      RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
      redisTemplate.setConnectionFactory(redisConnectionFactory);
      //使用fastjson序列化
      FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
      // value值的序列化采用fastJsonRedisSerializer
      redisTemplate.setValueSerializer(fastJsonRedisSerializer);
      redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
      // key的序列化采用StringRedisSerializer
      redisTemplate.setKeySerializer(new StringRedisSerializer());
      redisTemplate.setHashKeySerializer(new StringRedisSerializer());
      redisTemplate.setConnectionFactory(redisConnectionFactory);

      return redisTemplate ;
  }


  @Bean
  @ConditionalOnMissingBean(value = StringRedisTemplate.class)
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
      StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
      stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
      return stringRedisTemplate ;
  }
}
