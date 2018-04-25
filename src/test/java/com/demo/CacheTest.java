package com.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.KryoCodec;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.redisson.config.SentinelServersConfig;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.test.context.junit4.SpringRunner;
import com.demo.base.BaseTest;
import com.demo.model.Message;
import com.demo.service.MessageService;
import com.demo.utils.NumberUtil;
import com.demo.utils.StringUtil;
import redis.clients.jedis.Jedis;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTest extends BaseTest {
	
	@Value("${redis.global.sentienl1.ip}")
	private String sentienl1;
	@Value("${redis.global.sentienl.password}")
	private String password;
	
	@Value("${redisson.slaveReadOnly}")
    private boolean slaveReadOnly;
	@Value("${redisson.clusters}")
    private String redissonclusters;
	@Value("${redisson.sentinels}")
    private String redissonSentinels;
	@Value("${redisson.sentinel.masterName}")
    private String sentinelMasterName;
	@Value("${redisson.db}")
    private int db;
	@Value("${redisson.sentinel.password}")
    private String sentinelPassword;
	
	private String clusterCacheManagerName = "clusterCacheManager";
	
	private String sentinelCacheManagerName = "sentinelCacheManager";

	Jedis jedis;
	
	@Autowired
	private  MessageService messageService;
	
	@Before
	public void init() {
		jedis = new Jedis(sentienl1, 6379);		
	    
	    if(!password.isEmpty()){
	    	jedis.auth(password);
	    }
	    
	    jedis.connect();
	    jedis.select(db);
	}
	
	@Test
	public void jedisFirst() {

	    jedis.set("foo", "bar");
	    
	    jedis.flushAll();
	}

	@Test
	public void jedisJsonObject() {
		
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
		
	    Message readMessage = StringUtil.readJSON(value, Message.class);
		System.out.println(readMessage.getBody());
		
		jedis.flushAll();
	}
	
	@Test
	public void clusterRedisson(){
		Cache cache = getClusterCache();
		cache.clear();
		
		cache.put("key1", "hello Cluster Redisson!");
		String val = cache.get("key1").get().toString();
		System.out.println(val);
		cache.clear();
	} 
	
	@Test
	public void sentinelRedisson(){
		Cache cache = getSentinelCache();
		cache.put("key2", "hello Sentinel Redisson!");
		String val = cache.get("key2").get().toString();
		System.out.println(val);
		cache.clear();
	}
	
	@Test
	public void messageCache(){
		
		//先清除
		Cache cache =  getClusterCache();
		cache.clear();
		
		List<Message> messages = messageService.getType("mysql-dao");
		System.out.println(StringUtil.writeJSON(messages));
		
		messages = messageService.getTypeCache("mysql-dao");
		System.out.println(StringUtil.writeJSON(messages));
	}
	
	@Test
	public void messageCache2(){
		
		//先清除
		Cache cache =  getClusterCache();
		cache.clear();
		
		Message message = new Message();
		message.setReceiver("receiver");
		message.setType("mysql");
		
		List<Message> messages = messageService.getReceiverAndType(message);
		System.out.println(StringUtil.writeJSON(messages));
		
		messages = messageService.getReceiverAndTypeCache(message);
		System.out.println(StringUtil.writeJSON(messages));
	}
	
	@Test
	public void cacheEvict(){
		
		//先清除
		Cache cache =  getClusterCache();
		cache.clear();
		
		Message message = new Message();
		message.setReceiver("receiver");
		message.setSender("sender");
		message.setType("mysql");
		
		List<Message> receivers = messageService.getReceiverAndType(message);
		int bfCreateReceiverSize = receivers.size();
		
		receivers = messageService.getReceiverAndTypeCache(message);
		int bfCreateReceiverCacheSize =  receivers.size();
		
		List<Message> senders = messageService.getSenderAndType(message);
		int bfCreateSenderSize = senders.size();
		
		senders = messageService.getSenderAndTypeCache(message);
		int bfCreateSenderCacheSize = senders.size();
		
		message.setId(NumberUtil.randomLong());
		message.setMsgId(StringUtil.randomString(10));
		message.setLogDate(1L);
		message.setBody("new message!");
//		message.setReceiver("receiver");
//		message.setSender("sender");
		try {
			messageService.create(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		receivers = messageService.getReceiverAndType(message);
		int afCreateReceiverSize = receivers.size();
		
		receivers = messageService.getReceiverAndTypeCache(message);
		int afCreateReceiverCacheSize = receivers.size();
		
		senders = messageService.getSenderAndType(message);
		int afCreateSenderSize = senders.size();
		
		senders = messageService.getSenderAndTypeCache(message);
		int afCreateSenderCacheSize = senders.size();
		
		System.out.println("before create - receivers size:" + bfCreateReceiverSize);
		System.out.println("before create - receivers cache size:" + bfCreateReceiverCacheSize);	
		System.out.println("after create - receivers size:" + afCreateReceiverSize);
		System.out.println("after create - receivers cache size:" + afCreateReceiverCacheSize);
		
		System.out.println("before create - senders size:" + bfCreateSenderSize);
		System.out.println("before create - senders cache size:" + bfCreateSenderCacheSize);	
		System.out.println("after create - senders size:" + afCreateSenderSize);
		System.out.println("after create - senders cache size:" + afCreateSenderCacheSize);
	}
	
	@Test
	public void evict(){
		Cache cache =  getClusterCache();
		cache.clear();

		cache.put("key1", "hello Cluster Redisson!");
		String val = cache.get("key1").get().toString();
		System.out.println(val);
		
		cache.evict("key1");
		
		ValueWrapper vw =  cache.get("key1");
		if(vw!=null){
			val = vw.get().toString();
		}
		else{
			System.out.println("null");
		}
		
		cache.clear();
	}	
	
	@Test
	public void proxy(){
		//先清除
		Cache cache =  getClusterCache();
		cache.clear();
		
		Message message = new Message();
		message.setReceiver("receiver");
		message.setSender("sender");
		message.setType("mysql");
		
		List<Message> receivers = messageService.getReceiverAndType(message);
		int bfCreateReceiverSize = receivers.size();

		receivers = messageService.getReceiverAndTypeProxy(message);
		int afCreateReceiverSize = receivers.size();
		
		System.out.println("bf Size:" + bfCreateReceiverSize);
		System.out.println("af Size:" + afCreateReceiverSize);
		
		cache.clear();
	}
	
	private Cache getClusterCache(){
		RedissonSpringCacheManager cacheManager = getClusterRedisson();
		Cache cache = cacheManager.getCache(clusterCacheManagerName);
		return cache;
	}
	
	private Cache getSentinelCache(){
		RedissonSpringCacheManager cacheManager = getSentinelRedisson();
		Cache cache = cacheManager.getCache(sentinelCacheManagerName);
		return cache;
	}
	
	private RedissonSpringCacheManager getClusterRedisson(){
		String[] redisServers = redissonclusters.split(",");

		RedissonClient redissonClient = null;
		Config redissonConfig = new Config();
		KryoCodec codec = new KryoCodec();
		redissonConfig.setCodec(codec) //
				.useClusterServers() //
				.addNodeAddress(redisServers) // redis://10.16.179.15:6379,...
				.setReadMode(slaveReadOnly ? ReadMode.SLAVE : ReadMode.MASTER_SLAVE)
				.setScanInterval(2000)
				.setTimeout(10000);
		redissonClient = Redisson.create(redissonConfig);
		
		Map<String, CacheConfig> cacheConfig = new HashMap<>();
		// 設定"chatCache"緩存，過期時間ttl為30分，最長空閒時間maxIdleTime為12分
		cacheConfig.put("chatCache", new CacheConfig(30 * 60 * 1000, 12 * 60 * 1000));
		
		RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redissonClient,cacheConfig,codec);
		
		return redissonSpringCacheManager;
	}
	
	private RedissonSpringCacheManager getSentinelRedisson(){
		String[] redisServers = redissonSentinels.split(",");

		RedissonClient redissonClient = null;
		Config redissonConfig = new Config();
		KryoCodec codec = new KryoCodec();
		SentinelServersConfig sentinelServersConfig = redissonConfig.setCodec(codec).useSentinelServers();
		sentinelServersConfig.setMasterName(sentinelMasterName) //
				.setDatabase(db) //
				.setPassword(sentinelPassword) //
				.addSentinelAddress(redisServers) //
				.setReadMode(slaveReadOnly ? ReadMode.SLAVE : ReadMode.MASTER_SLAVE) //
				.setTimeout(10000);
		redissonClient = Redisson.create(redissonConfig);
		
		Map<String, CacheConfig> cacheConfig = new HashMap<>();
		// 設定"chatCache"緩存，過期時間ttl為30分，最長空閒時間maxIdleTime為12分
		cacheConfig.put("chatCache", new CacheConfig(30 * 60 * 1000, 12 * 60 * 1000));
		
		RedissonSpringCacheManager redissonSpringCacheManager = new RedissonSpringCacheManager(redissonClient,cacheConfig,codec);
		
		return redissonSpringCacheManager;
	}	
}