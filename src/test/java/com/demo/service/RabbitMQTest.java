package com.demo.service;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.base.BaseTest;
import com.demo.service.rabbitmq.RabbitService;


public class RabbitMQTest extends BaseTest {
	
//	@Rule
//	public BenchmarkRule benchmarkRule = new BenchmarkRule();
	
	@Autowired
	private RabbitService service;
	
	@Test
	public void sendMsg() {
		
		service.recv("queueName");
		
		service.send("queueName", "message");
		
		while (true) {

		}
	}
	
	@Test
	public void sendMsgs() {
		
		service.recv("queueName");
		
		int i = 0;
		while (i < 20000) {
			service.send("queueName", "message");
			i++;
		}
		
		while (true) {

		}
	}

}
