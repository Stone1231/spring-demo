package com.demo.service;

import static org.junit.Assert.fail;

import javax.annotation.Resource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.model.Feeback;
import com.demo.model.Message;
import com.demo.service.kafka.MessageKafkaReceiveService;
import com.demo.service.kafka.MessageKafkaSendService;
import com.demo.service.kafka.base.KafkaBaseReceiveListener;
import com.demo.utils.StringUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaTest {
	
	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();
	
	@Resource
	private MessageKafkaSendService sendService;
	
	@Resource
	MessageKafkaReceiveService receiveService;	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void sendMessage() throws Exception {

		String id = StringUtil.randomString(10);
		
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("test");
		message.setBody("hi kafka!");
		message.setReceiver("receiver");
		message.setSender("sender");
		
		sendService.sendMessage(id, message);
	}
	
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void receiver() throws Exception {
		receiveService.messageReceiver(new KafkaBaseReceiveListener<Message>(){
			
			@Override
			public Feeback receiveMessage(String key, Message message) {
				// TODO Auto-generated method stub
				System.out.print(
						String.format("My receiveMessage (key, message)\n key:%s \n message:%s\n"
						, key, StringUtil.writeJSON(message)));					
				return new Feeback(Feeback.Status.success, "測試");				
			}
			
		});

		Thread.sleep(1000000);
		receiveService.shutdown();
	}
}
