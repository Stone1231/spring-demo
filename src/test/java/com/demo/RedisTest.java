package com.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;


import org.aspectj.weaver.NewConstructorTypeMunger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.base.BaseTest;
import com.demo.model.Message;
import com.demo.utils.StringUtil;
import com.demo.utils.StringUtil3;
import com.google.common.reflect.TypeToken;
import com.demo.utils.AssertUtil;
import com.demo.utils.IoUtil;
import com.demo.utils.SerializeUtil;
import java.lang.reflect.Type;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest extends BaseTest {
	
	Jedis jedis;
	
	@Before
	public void init() {
		jedis = new Jedis("10.16.179.205", 6379);		
	    String pwd = "backend";
	    	    
	    if(!pwd.isEmpty()){
	    	jedis.auth(pwd);
	    	jedis.connect();
	    }
	    jedis.select(59);
	}
	
	@Test
	public void testJedisFirst() {
	    //Boolean isconn = jedis.isConnected();
	    //System.out.println("Connected to Redis %s",);
	    
	    jedis.set("foo", "bar");
	    
	    jedis.flushAll();
	    
//	    String value = jedis.get("foo");
//	    System.out.println(String.format("foo [%s] ", value));
	    
	    //jedis.del("foo");
	    
	    //jedis.close();
	    //System.out.println(String.format("Connected to Redis [%s] ", isconn));
		
		 
//		 config.setMaxIdle(8);
//	        config.setMaxTotal(8);
//	        config.setMaxWaitMillis(500);
//	        config.setTestOnReturn(true);
		 
		
		//JedisSentinelPool pool = new JedisSentinelPool();
		//Jedis jedis = pool.getResource();
		
	}

	@Test
	public void testJedisJsonObject() {
		
		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("test");
		message.setBody("hi redis!");
		message.setReceiver("receiver");
		message.setSender("sender");

		jedis.flushAll();
	    
	    jedis.set("key1", StringUtil.writeJSON(message));
	    String value = jedis.get("key1");
		
	    Message cacheMessage = StringUtil.readJSON(value, Message.class);
		System.out.println(cacheMessage.getBody());
	}
	
	@Test
	public void testJsonGenericsObject() {
	    RedisTest.CacheObject<String> cacheObject = new RedisTest.CacheObject<>("value", 5);
	    
	    String str = StringUtil.writeJSON(cacheObject);
	    
	    RedisTest.CacheObject<String> cacheObject2 = StringUtil.readJSON(str,cacheObject.getClass());
	}
	
	@Test
	public void testJsonGenericsCollection(){

        CacheObject<Collection<Message>> cacheObject = new CacheObject<Collection<Message>>();

        Collection<Message> messages = new ArrayList<Message>() {{
			new Message().setBody("1-message");
			new Message().setBody("2-message");
			new Message().setBody("3-message");
		}};
		
		cacheObject.object = messages;
		
		Type type = new TypeToken<CacheObject<Collection<Message>>>(){}.getType();
		String value = StringUtil3.writeJSON(cacheObject, type);

		CacheObject<Collection<Message>> cacheObject2 = StringUtil3.readJSON(value, type);
		
		Collection<Message> cacheMessages = cacheObject2.object;	
		for (Message item : cacheMessages) {
			System.out.println(item.getBody());
		}
	}
	
	@Test
	public void testJedisKryoObject() {
		
		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("test");
		message.setBody("test JedisKryoObject");
		message.setReceiver("receiver");
		message.setSender("sender");

	    String keyString = "key1";

	    byte[] keyByte =  SerializeUtil.kryoWriteClass(keyString);
	    byte[] valueByte =  SerializeUtil.kryoWriteClass(message);
	    
	    jedis.flushAll();
	    jedis.set(keyByte, valueByte);
	    
	    byte[] resultByte =  jedis.get(keyByte);
	    
	    Message rtnValue = SerializeUtil.dekryoReadClass(resultByte);
		System.out.println(rtnValue.getBody());
	}

	@Test
	public void testJedisKryGenericsObject() {
		
		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("test");
		message.setBody("jedisKryoObject");
		message.setReceiver("receiver");
		message.setSender("sender");
	    
	    jedis.flushAll();

	    //jedis.set(key, value)
	    //jedis.auth("password");
	    //jedis.connect();
	    //Boolean isconn = jedis.isConnected();
	    //System.out.println("Connected to Redis %s",);

	    CacheObject<Message> cacheObject = new CacheObject<Message>(message, 10);

	    byte[] dbByte =  SerializeUtil.kryoWriteClass("db1");

//        Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
//        jedis.hmset(dbByte, hash);
        
        ///
	    byte[] keyByte =  SerializeUtil.kryoWriteClass("k1");
	    byte[] valueByte =  SerializeUtil.kryoWriteClass(cacheObject);
	    
	    jedis.hset(dbByte, keyByte, valueByte);	    
	    byte[] resultByte =  jedis.hget(dbByte,keyByte);
	    
	    CacheObject<Message> rtnValue = SerializeUtil.dekryoReadClass(resultByte);
		System.out.println(rtnValue.object.getBody());
        
        ///
		message.setBody("jedisKryoObject-2");
        cacheObject = new CacheObject<Message>(message,20);
        
	    keyByte =  SerializeUtil.kryoWriteClass("k2");
	    valueByte =  SerializeUtil.kryoWriteClass(cacheObject);
	    
	    jedis.hset(dbByte, keyByte, valueByte);	    
	    resultByte = jedis.hget(dbByte,keyByte);
	    
	    rtnValue = SerializeUtil.dekryoReadClass(resultByte);
		System.out.println(rtnValue.object.getBody());	    
	    ///
		message.setBody("jedisKryoObject-3");
        cacheObject = new CacheObject<Message>(message, 30);
        
	    keyByte =  SerializeUtil.kryoWriteClass("k3");
	    valueByte =  SerializeUtil.kryoWriteClass(cacheObject);
	    
	    jedis.hset(dbByte, keyByte, valueByte);
	    resultByte =  jedis.hget(dbByte,keyByte);
	    
	    rtnValue = SerializeUtil.dekryoReadClass(resultByte);
		System.out.println(rtnValue.object.getBody());	
		
		///del
		jedis.hdel(dbByte, keyByte);
		
		Boolean exist =	jedis.hexists(dbByte, keyByte);
		System.out.println(exist);
	    /// entrySet
        for (final Entry<byte[], byte[]> entry : jedis.hgetAll(dbByte).entrySet()) {            	
        	String k = SerializeUtil.dekryoReadClass(entry.getKey());            	
        	CacheObject<Message> v = SerializeUtil.dekryoReadClass(entry.getValue());
        	System.out.println(String.format("[getKey()] %s [getValue()] %s", k, v.object.getBody()));
        }  
        /// hkeys
        //???
        Set<byte[]> aa = jedis.hkeys(dbByte);
        Collection<byte[]> bb = jedis.hvals(dbByte);
		
		/// another db
		dbByte =  SerializeUtil.kryoWriteClass("db2");
		
		message.setBody("jedisKryoObject-4");;
        cacheObject = new CacheObject<Message>(message,30);
        
	    keyByte =  SerializeUtil.kryoWriteClass("k3");
	    valueByte =  SerializeUtil.kryoWriteClass(cacheObject);
	    
	    jedis.hset(dbByte, keyByte, valueByte);
	    resultByte =  jedis.hget(dbByte,keyByte);
	    
	    rtnValue = SerializeUtil.dekryoReadClass(resultByte);
		System.out.println(rtnValue.object.getBody());	
	    ///
	    
		//jedis.hdel(keyByte);
		jedis.del(dbByte);
	}
	
	@Test
	public void testJedisKryGenericsObject2() {
		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("redis");
		message.setBody("JedisKryGenericsObject2");
		message.setReceiver("receiver");
		message.setSender("sender");

	    Jedis jedis = new Jedis("localhost", 6379);
	    jedis.flushAll();

	    byte[] dbByte =  SerializeUtil.kryoWriteClass("db1");

//        Map<byte[], byte[]> hash = new HashMap<byte[], byte[]>();
//        jedis.hmset(dbByte, hash);
        
        ///
	    byte[] keyByte =  SerializeUtil.kryoWriteClass("key 1");
	    byte[] valueByte =  SerializeUtil.kryoWriteClass(message);
	    
	    jedis.hset(dbByte, keyByte, valueByte);	    
	    byte[] resultByte =  jedis.hget(dbByte,keyByte);
	    
	    Message rtnValue = SerializeUtil.dekryoReadClass(resultByte);
		System.out.println(rtnValue.getBody());
        
        ///
		message = new Message();
		message.setBody("JedisKryGenericsObject2-2");

	    keyByte =  SerializeUtil.kryoWriteClass("key 2");
	    valueByte =  SerializeUtil.kryoWriteClass(message);
	    
	    jedis.hset(dbByte, keyByte, valueByte);	    
	    resultByte = jedis.hget(dbByte,keyByte);
	    
	    rtnValue = SerializeUtil.dekryoReadClass(resultByte);
		System.out.println(rtnValue.getBody());	    
	    ///
		message = new Message();
		message.setBody("JedisKryGenericsObject2-3");

	    keyByte =  SerializeUtil.kryoWriteClass("key 3");
	    valueByte =  SerializeUtil.kryoWriteClass(message);
	    
	    jedis.hset(dbByte, keyByte, valueByte);
	    resultByte =  jedis.hget(dbByte,keyByte);
	    
	    rtnValue = SerializeUtil.dekryoReadClass(resultByte);
		System.out.println(rtnValue.getBody());	
		
		byte[] queryKeyByte =  SerializeUtil.kryoWriteClass("key*");
		jedis.hmget(dbByte, keyByte);
		jedis.hget(dbByte, queryKeyByte);
		///del
		jedis.hdel(dbByte, keyByte);
		//jedis.hkeys(key)
		
		Boolean exist =	jedis.hexists(dbByte, keyByte);
		System.out.println(exist);
	    /// entrySet
        for (final Entry<byte[], byte[]> entry : jedis.hgetAll(dbByte).entrySet()) {            	
        	String k = SerializeUtil.dekryoReadClass(entry.getKey());            	
        	Message v = SerializeUtil.dekryoReadClass(entry.getValue());
        	System.out.println(String.format("[getKey()] %s [getValue()] %s", k, v.getBody()));
        }
	}
	
	@Test
	public void testJedisJsonGenericsObject() {
		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("redis");
		message.setBody("JedisKryGenericsObject2");
		message.setReceiver("receiver");
		message.setSender("sender");

	    Jedis jedis = new Jedis("localhost", 6379);

	    //jedis.set(key, value)
	    //jedis.auth("password");
	    //jedis.connect();
	    //Boolean isconn = jedis.isConnected();
	    //System.out.println("Connected to Redis %s",);

	    String keyString = "key1";
	    
	    CacheObject<Message> cacheObject = new CacheObject<Message>(message,10);
	    
		String value = StringUtil.writeJSON(cacheObject);
 	    
	    jedis.set(keyString, value);

	    String rtnValue = jedis.get(keyString);

	    CacheObject<Message> cacheObject2 = StringUtil.readJSON(rtnValue,cacheObject.getClass());
	    
		System.out.println(cacheObject2.object.getBody());
	}
	
	@Test
	public void testJsonTokenGenericsObject(){

		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("redis");
		message.setBody("JedisKryGenericsObject2");
		message.setReceiver("receiver");
		message.setSender("sender");

	    Jedis jedis = new Jedis("localhost", 6379);

	    String keyString = "key1";
	    
	    CacheObject<Message> cacheObject = new CacheObject<Message>(message,10);
	    	    
	    
		Type type = new TypeToken<CacheObject<Message>>(){}.getType();
	    
		String value = StringUtil3.writeJSON(cacheObject, type);
 	    
	    jedis.set(keyString, value);
	    
	    String rtnValue = jedis.get(keyString);

	    CacheObject<Message> cacheObject2 = StringUtil3.readJSON(rtnValue, type);
	    
		System.out.println(cacheObject2.object.getBody());
	}

	@Test
	public void testKryoMessage() {
		
		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("kryo");
		message.setBody("KryoMessage");
		message.setReceiver("receiver");
		message.setSender("sender");

	    byte[] valueByte =  SerializeUtil.kryoWriteClass(message);
	    
	    Message rtnValue = SerializeUtil.dekryoReadClass(valueByte);
		System.out.println(rtnValue.getBody());		
	}	
	
	
	@Test
	public void testKryoMessageCollection(){
		
        Collection<Message> list = new ArrayList<Message>() {{
			new Message().setBody("1-message");
			new Message().setBody("2-message");
			new Message().setBody("3-message");
		}};

		Message msg = list.iterator().next();
		System.out.println(String.format("get messageBody %s", msg.getBody()));

	    byte[] valueByte =  SerializeUtil.kryoWriteClass(list);
	    
	    Collection<Message> rtnValue = SerializeUtil.dekryoReadClass(valueByte);
		System.out.println(rtnValue.iterator().next().getBody());				
	}
	
	
    private static class CacheObject<V> {

        /**
         * Underlying object wrapped by the CacheObject.
         */
        public V object;

        /**
         * The size of the Cacheable object. The size of the Cacheable
         * object is only computed once when it is added to the cache. This makes
         * the assumption that once objects are added to cache, they are mostly
         * read-only and that their size does not change significantly over time.
         */
        public int size;

        /**
         * A reference to the node in the cache order list. We keep the reference
         * here to avoid linear scans of the list. Every time the object is
         * accessed, the node is removed from its current spot in the list and
         * moved to the front.
         */
        //public LinkedListNode<?> lastAccessedListNode;

        /**
         * A reference to the node in the age order list. We keep the reference
         * here to avoid linear scans of the list. The reference is used if the
         * object has to be deleted from the list.
         */
        //public LinkedListNode<?> ageListNode;

        /**
         * A count of the number of times the object has been read from cache.
         */
        public int readCount = 0;

        /**
         * Creates a new cache object wrapper. The size of the Cacheable object
         * must be passed in in order to prevent another possibly expensive
         * lookup by querying the object itself for its size.<p>
         *
         * @param object the underlying Object to wrap.
         * @param size   the size of the Cachable object in bytes.
         */
        public CacheObject(V object, int size) {
            this.object = object;
            this.size = size;
        }
        
        public CacheObject() {

        }        
    }
}
