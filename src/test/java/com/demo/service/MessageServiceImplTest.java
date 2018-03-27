package com.demo.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.demo.base.BaseTest;
import com.demo.model.Message;

public class MessageServiceImplTest extends BaseTest {

	@Autowired
	private  MessageService messageService;
	
	@Test
	public void test(){
		
		List<Message> messages = messageService.getbyType("mysql-dao");
		
		System.out.println(messages.size());
	}
}
