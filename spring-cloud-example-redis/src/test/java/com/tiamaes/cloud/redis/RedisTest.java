package com.tiamaes.cloud.redis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
	private static Logger logger = LoggerFactory.getLogger(RedisTest.class);
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Before
	public void before() {
		assertNotNull("redisTemplate not found...", redisTemplate);
	}
	
	@Test
	public void testOpsForValue(){
		ValueOperations<String, String> operator1 = redisTemplate.opsForValue();
		
		String key1 = "tiamaes.cloud.redis.value";
		String expected = "192.168.0.124";
		
		operator1.set(key1, expected);
		Object actual1 = operator1.get(key1);
		logger.debug(actual1.toString());
		assertEquals(expected, actual1);
		
		ValueOperations<String, String> operator2 = stringRedisTemplate.opsForValue();
		actual1 = operator2.get(key1);
		assertEquals(expected, actual1);
		
	}
	
	
	@Test
	public void testOpsForHash(){
		HashOperations<String, String, Object> operator = redisTemplate.opsForHash();
		String key = "tiamaes.cloud.redis.hash";
		String hashKey = "string";
		String expected = "192.168.0.124";
		
		operator.put(key, hashKey, expected);
		Object actual = operator.get(key, hashKey);
		logger.debug(actual.toString());
		
		assertTrue(expected.equals(actual));
		assertEquals(expected, actual);
		
		operator.put(key, hashKey, 999);
		operator.increment(key, hashKey, 1);
		actual = operator.get(key, hashKey);
		assertTrue(1000 == Integer.parseInt(actual.toString()));
		
		
		HashOperations<String, String, Object> operator2 = stringRedisTemplate.opsForHash();
		operator2.put(key, hashKey, expected);
		
		Object actual1 = operator.get(key, hashKey);
		Object actual2 = operator2.get(key, hashKey);
		
		assertTrue(actual1.equals(actual2));
		
		
		operator2.put(key, hashKey, 999);
		operator.increment(key, hashKey, 1);
		actual = operator2.get(key, hashKey);
		assertTrue(1000 == Integer.parseInt(actual.toString()));
		
		
		operator2.put(key, hashKey, 999);
		actual = operator2.get(key, hashKey);
		assertTrue(999 == Integer.parseInt(actual.toString()));
		
		operator2.put(key, hashKey, "00999");
		actual = operator2.get(key, hashKey);
		assertTrue("00999".equals(actual.toString()));
	}
	
	@Test
	public void testOpsForSet(){
		SetOperations<String, String> operator = redisTemplate.opsForSet();
		String key = "tiamaes.cloud.redis.set";
		operator.add(key, "123456");
		operator.add(key, "234567");
		
		long size = operator.size(key);
		assertTrue(size == 2);
		
		assertTrue(operator.isMember(key, "123456"));
		
		String key2 = "tiamaes.cloud.redis.set2";
		operator.add(key2, "123456");
		
		Set<String> set1 = operator.members(key);
		Set<String> set2 = operator.union(key2, set1);
		
		assertTrue(set2.size() == 1);
		logger.debug(set2.toString());
	}
}
