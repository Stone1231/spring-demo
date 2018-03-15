package com.demo.dao;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.dao.mongo.MessageMongoDao;
import com.demo.model.Message;
import com.demo.utils.StringUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTest {
	
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();
	
	@Autowired
	private MessageMongoDao messageMongoDao;

	@Test
	public void saveAndQuery(){
		
		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("test");
		message.setBody("body");
		message.setReceiver("receiver");
		message.setSender("sender");

		try {
			messageMongoDao.save(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
		    List<Message> list =messageMongoDao.queryById(id);
		    if(list.size() >0){
		    	assertTrue(list.get(0).getMsgId().equals(id));
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
