package com.demo.dao;


import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.base.BaseTest;
import com.demo.dao.mysql.MessageMysqlDao;
import com.demo.model.Message;
import com.demo.utils.NumberUtil;
import com.demo.utils.StringUtil;


public class MysqlTest extends BaseTest {
	
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();	
	
	@Autowired
	private MessageMysqlDao messageMysqlDao;
	
	@BenchmarkOptions(benchmarkRounds = 3, warmupRounds = 0, concurrency = 1)
	@Test
	public void testMysqlDao(){
		//String type = "mysql-dao";
		String type = "mysql";
		
		String msgId = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setId(NumberUtil.randomLong());
		message.setMsgId(msgId);
		message.setLogDate(1L);
		message.setType(type);
		message.setBody("body");
		message.setReceiver("receiver");
		message.setSender("sender");
		
		messageMysqlDao.insert(message);
		
		message.setBody("body2");
		messageMysqlDao.insert2(message);
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
