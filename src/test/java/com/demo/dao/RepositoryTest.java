package com.demo.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.demo.base.BaseTest;
import com.demo.dao.mysql.MessageRepository;
import com.demo.model.Message;
import com.demo.utils.NumberUtil;
import com.demo.utils.StringUtil;


public class RepositoryTest extends BaseTest {
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Test
	public void testRepository(){
		
		String type = "repository";
		
		String msgId = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(msgId);
		message.setLogDate(1L);
		message.setType(type);
		message.setBody("body");
		message.setReceiver("receiver");
		message.setSender("sender");
		
		messageRepository.save(message);
		
		msgId = StringUtil.randomString(10);
		Message message2 = new Message();
		message2.setMsgId(msgId);
		message2.setLogDate(3L);
		message2.setType(type);
		message2.setBody("body");
		message2.setReceiver("receiver");
		message2.setSender("sender");
		
		messageRepository.save(message2);
		
		List<Message> messages =  messageRepository.findByType(type);
		
		Boolean check = messages.size() == 2;
		assertTrue(check);
		
		check = messages.get(0).getType().equals(type);
		assertTrue(check);
		
		messages =  messageRepository.findByMsgIdAndType(msgId, type);
		check = messages.size() == 1;
		assertTrue(check);
		
		long count = messageRepository.countByType(type);
		check = count == 2;
		assertTrue(check);

		count = messageRepository.deleteByType(type);
		check = count == 2;//移除掉的筆數
		assertTrue(check);
		
		messageRepository.save(message);
		messageRepository.save(message2);
		
		messages = messageRepository.customQuery(2L);
		check = messages.size() == 1;
		assertTrue(check);
		
		messages =  messageRepository.removeByType(type);//移除掉的資料
		check = messages.size() == 2;
		assertTrue(check);
		
		count = messageRepository.countByType(type);
		check = count == 0;
		assertTrue(check);
	}
}
