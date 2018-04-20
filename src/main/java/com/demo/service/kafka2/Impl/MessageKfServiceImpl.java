package com.demo.service.kafka2.Impl;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.demo.model.Message;
import com.demo.service.kafka2.MessageKfService;


@Service
public class MessageKfServiceImpl extends BaseKfServiceImpl<Message> implements MessageKfService {

	@PostConstruct
	protected void init() {
		super.init();
	}
	
	
	
}
