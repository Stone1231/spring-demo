package com.demo.dao;


import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.base.BaseTest;
import com.demo.dao.mysql.MessageMysqlDao;
import com.demo.model.Message;
import com.demo.utils.NumberUtil;
import com.demo.utils.StringUtil;

import io.netty.util.ThreadDeathWatcher;


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
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	@Test
	public void transaction(){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = transactionManager.getTransaction(def);
		
	    try {
	    	
			String type = "trans";
			
			String msgId = StringUtil.randomString(10);
			
			Message message = new Message();
			message.setId(NumberUtil.randomLong());
			message.setMsgId(msgId);
			message.setLogDate(2L);
			message.setType(type);
			message.setBody("body");
			message.setReceiver("receiver");
			message.setSender("sender");
			
			messageMysqlDao.insert(message);
			
			List<Message> messages =  messageMysqlDao.getbyType(type);
			
			//assertTrue(false);
			
			msgId = StringUtil.randomString(10);
			Message message2 = new Message();
			message2.setId(NumberUtil.randomLong());
			message2.setMsgId(msgId);
			message2.setLogDate(3L);
			message2.setType(type);
			message2.setBody("body");
			message2.setReceiver("receiver");
			message2.setSender("sender");
			
			if(true){
				throw new Exception("error");
			}			
			messageMysqlDao.insert(message2);
	        
	        transactionManager.commit(status);
	    }
	    catch (Exception ex) {
	        transactionManager.rollback(status);
	    }
	}
}
