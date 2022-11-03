package com.etjava.config;


import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.Duration;

/**
 * Redis配置类
 * 
 * @author Administrator
 *
 */
@Configuration
public class RedisConfig {


	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.port}")
	private Integer port;
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.lettuce.pool.max-idle}")
	private Integer maxIdle;
	@Value("${spring.redis.lettuce.pool.min-idle}")
	private Integer minIdle;
	@Value("${spring.redis.lettuce.pool.max-active}")
	private Integer maxTotal;
	@Value("${spring.redis.lettuce.pool.max-wait}")
	private Duration maxWaitMillis;


	@Bean
	LettuceConnectionFactory lettuceConnectionFactory() {
		// 连接池配置
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(maxIdle == null ? 8 : maxIdle);
		poolConfig.setMinIdle(minIdle == null ? 1 : minIdle);
		poolConfig.setMaxTotal(maxTotal == null ? 8 : maxTotal);
		poolConfig.setMaxWaitMillis(maxWaitMillis == null ? 5000L : maxWaitMillis.toMillis());
		LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration.builder()
				.poolConfig(poolConfig)
				.build();
		// 单机redis
		RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
		redisConfig.setHostName(host);
		redisConfig.setPort(port);
		if (password != null && !"".equals(password)) {
			redisConfig.setPassword(password);
		}

		// redisConfig.setPassword(password);

		return new LettuceConnectionFactory(redisConfig, lettucePoolingClientConfiguration);
	}


	/**
	 * 使用自定义的序列化类
	 * @param lettuceConnectionFactory
	 * @return
	 */
	@Bean(name="redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory);
		//自定义序列化类
		MyRedisSerializer myRedisSerializer = new MyRedisSerializer();
		//key序列化方式
		template.setKeySerializer(new StringRedisSerializer());
		//value序列化
		template.setValueSerializer(myRedisSerializer);
		//value hashmap序列化
		template.setHashValueSerializer(myRedisSerializer);
		return template;
	}

	/**
	 * 使用原生的序列化类进行序列化
	 * @param lettuceConnectionFactory
	 * @return
	 */
	@Bean(name="redisTemplate2")
	public RedisTemplate<String, Object> redisTemplate2(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(lettuceConnectionFactory);
		//key序列化方式 - 原生序列化类StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		//value序列化 - 原生序列化类StringRedisSerializer
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}
	
	/**
	 * 使用原生的序列化类 - 将数据存放到下标6的库中
	 * @param lettuceConnectionFactory
	 * @return
	 */
	@Bean(name="redisTemplate3")
	public RedisTemplate<String, Object> redisTemplate3(LettuceConnectionFactory lettuceConnectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		LettuceConnectionFactory lettuceConnectionFactoryNew =  new LettuceConnectionFactory(lettuceConnectionFactory.getStandaloneConfiguration());

		lettuceConnectionFactoryNew.setDatabase(6);
		lettuceConnectionFactoryNew.afterPropertiesSet();
		template.setConnectionFactory(lettuceConnectionFactoryNew);
		//key序列化方式 - 原生序列化类StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		//value序列化 - 原生序列化类StringRedisSerializer
		template.setValueSerializer(new StringRedisSerializer());
		return template;
	}

	static class MyRedisSerializer implements RedisSerializer<Object> {

		@Override
		public byte[] serialize(Object o) throws SerializationException {
			return serializeObj(o);
		}

		@Override
		public Object deserialize(byte[] bytes) throws SerializationException {
			return deserializeObj(bytes);
		}

		/**
		 * 序列化
		 * @param object
		 * @return
		 */
		private static byte[] serializeObj(Object object) {
			ObjectOutputStream oos = null;
			ByteArrayOutputStream baos = null;
			try {
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				byte[] bytes = baos.toByteArray();
				return bytes;
			} catch (Exception e) {
				throw new RuntimeException("序列化失败!", e);
			}
		}

		/**
		 * 反序列化
		 * @param bytes
		 * @return
		 */
		private static Object deserializeObj(byte[] bytes) {
			if (bytes == null){
				return null;
			}
			ByteArrayInputStream bais = null;
			try {
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			} catch (Exception e) {
				throw new RuntimeException("反序列化失败!", e);
			}
		}

	}
}