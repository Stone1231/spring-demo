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

import com.demo.dao.mysql.MessageMysqlDao;
import com.demo.model.Message;
import com.demo.utils.StringUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MysqlTest {
	
	@Autowired
	private MessageMysqlDao messageMysqlDao;
	
	@Test
	public void saveQueryDel(){
		
		String type = "mysql";
		
		String msgId = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(msgId);
		message.setLogDate(1L);
		message.setType(type);
		message.setBody("body");
		message.setReceiver("receiver");
		message.setSender("sender");
		
		messageMysqlDao.save(message);
		
		msgId = StringUtil.randomString(10);
		Message message2 = new Message();
		message2.setMsgId(msgId);
		message2.setLogDate(1L);
		message2.setType(type);
		message2.setBody("body");
		message2.setReceiver("receiver");
		message2.setSender("sender");
		
		messageMysqlDao.save(message2);
		
		List<Message> messages =  messageMysqlDao.findByType(type);
		
		Boolean check = messages.size() == 2;
		assertTrue(check);
		
		check = messages.get(0).getType().equals(type);
		assertTrue(check);
		
		messages =  messageMysqlDao.findByMsgIdAndType(msgId, type);
		check = messages.size() == 1;
		assertTrue(check);
		
		long count = messageMysqlDao.countByType(type);
		check = count == 2;
		assertTrue(check);

		count = messageMysqlDao.deleteByType(type);
		check = count == 2;//移除掉的筆數
		assertTrue(check);
		
		messageMysqlDao.save(message);
		messageMysqlDao.save(message2);
		messages =  messageMysqlDao.removeByType(type);//移除掉的資料
		check = messages.size() == 2;
		assertTrue(check);
		
		count = messageMysqlDao.countByType(type);
		check = count == 0;
		assertTrue(check);
	}
	
//	@Autowired
//	private DataSourceTransactionManager transactionManager;
	
	
	@Test
	public void transaction(){
//		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//		TransactionStatus status = transactionManager.getTransaction(def);
//		
//	    try {
//	    	
//			String type = "trans";
//			
//			String msgId = StringUtil.randomString(10);
//			
//			Message message = new Message();
//			message.setMsgId(msgId);
//			message.setLogDate(1L);
//			message.setType(type);
//			message.setBody("body");
//			message.setReceiver("receiver");
//			message.setSender("sender");
//			
//			messageMysqlDao.save(message);
//			
//			List<Message> messages =  messageMysqlDao.findByType(type);
//			
//			assertTrue(false);
//			
//			msgId = StringUtil.randomString(10);
//			Message message2 = new Message();
//			message2.setMsgId(msgId);
//			message2.setLogDate(1L);
//			message2.setType(type);
//			message2.setBody("body");
//			message2.setReceiver("receiver");
//			message2.setSender("sender");
//			
//			messageMysqlDao.save(message2);
//	        
//	        transactionManager.commit(status);
//	    }
//	    catch (Exception ex) {
//	        transactionManager.rollback(status);
//	    }
	}
}
