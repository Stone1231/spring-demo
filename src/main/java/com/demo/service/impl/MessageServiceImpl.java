package com.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.osgi.framework.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.cache.anno2.Cacheable;
import com.demo.cache.anno2.Caching;
import com.demo.cache.anno2.CacheEvict;
import com.demo.dao.mysql.MessageMysqlDao;
import com.demo.model.Message;
import com.demo.service.MessageService;


@Service
public class MessageServiceImpl implements MessageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

	@Autowired
	private MessageMysqlDao messageMysqlDao;

	@Override
	@Cacheable(cacheManager = "clusterCacheManager", cacheNames = { "messageType" },
			// hashFlag = true, 每次更新?
			key = "#type")
	public List<Message> getType(String type) {
		LOGGER.info("into getbyType");
		return messageMysqlDao.getbyType(type);
	}

	@Override
	@Cacheable(cacheManager = "clusterCacheManager", cacheNames = { "messageType" },
			// hashFlag = true, 每次更新?
			key = "#type")
	public List<Message> getTypeCache(String type) {
		LOGGER.info("into getCache");
		return null;
	}

	@Override
	@Cacheable(cacheManager = "clusterCacheManager", cacheNames = { com.demo.cache.CacheNames.SENDER },
			// hashFlag = true,
			key = "T(com.demo.cache.CacheNames).SENDER + ':'+ #message.sender + '_' + #message.type")
	public List<Message> getSenderAndType(Message message) {
		LOGGER.info("into getSenderAndType");
		return messageMysqlDao.getSender(message.getSender(), message.getType());
	}

	@Override
	@Cacheable(cacheManager = "clusterCacheManager", cacheNames = { com.demo.cache.CacheNames.SENDER },
			// hashFlag = true,
			key = "T(com.demo.cache.CacheNames).SENDER + ':'+ #message.sender + '_' + #message.type")
	public List<Message> getSenderAndTypeCache(Message message) {
		LOGGER.info("into getSenderAndTypeCache");
		return new ArrayList<>();
	}

	@Override
	@Cacheable(cacheManager = "clusterCacheManager", cacheNames = { com.demo.cache.CacheNames.RECEIVER },
			// hashFlag = true,
			key = "T(com.demo.cache.CacheNames).RECEIVER + ':'+ #message.receiver + '_' + #message.type")
	public List<Message> getReceiverAndType(Message message) {
		LOGGER.info("into getReceiverAndType");
		return messageMysqlDao.getReceiver(message.getReceiver(), message.getType());
	}

	@Override
	@Cacheable(cacheManager = "clusterCacheManager", cacheNames = { com.demo.cache.CacheNames.RECEIVER }, // 需要加function參數才用"T(com.demo.cache.CacheNames).RECEIVER"
			// hashFlag = true,
			key = "T(com.demo.cache.CacheNames).RECEIVER + ':'+ #message.receiver + '_' + #message.type")
	public List<Message> getReceiverAndTypeCache(Message message) {
		LOGGER.info("into getReceiverAndTypeCache");
		return new ArrayList<>();
	}

	@Override
	public List<Message> getReceiverAndTypeProxy(Message message) {
		LOGGER.info("into getReceiverAndTypeProxy");
		return ((MessageService) AopContext.currentProxy()).getReceiverAndType(message);
	}

	@Override
	@Caching(evict = {
			@CacheEvict(cacheManager = "clusterCacheManager", cacheNames = {
					com.demo.cache.CacheNames.SENDER }, key = "T(com.demo.cache.CacheNames).SENDER + ':'+ #message.sender + '_' + #message.type"),
			@CacheEvict(cacheManager = "clusterCacheManager", cacheNames = {
					com.demo.cache.CacheNames.RECEIVER }, key = "T(com.demo.cache.CacheNames).RECEIVER + ':'+ #message.receiver + '_' + #message.type") })
	public void create(Message message) {
		messageMysqlDao.insert(message);

		// TODO trans 還沒實作成功
		// test trans
		// message.setLogDate(DateUtil2.nowUTCTimestamp());
		// if(true){
		// throw new ServiceException("rollback");
		// }
		messageMysqlDao.update(message);
	}

	// 下面是測試Mocks用
	@Override
	public Message convertMsg(Message messsage) throws Exception {

		String msgType = messsage.getType();

		List<Message> messsages = messageMysqlDao.getbyType(msgType);
		Message firstMesssage = messsages.get(0);

		String newMsg = privateMsg(firstMesssage.getBody());
		firstMesssage.setBody(newMsg);
		
		Integer res = messageMysqlDao.update(firstMesssage);
		firstMesssage.setMsgId(res.toString());

		return firstMesssage;
	}

	private String privateMsg(String msg) throws Exception {
		if (msg.equals("error")) {
			throw new Exception("privateMsg error");
		}

		return "<" + msg + ">";
	}

}
