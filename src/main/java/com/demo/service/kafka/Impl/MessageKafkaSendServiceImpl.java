package com.demo.service.kafka.Impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.model.Message;
import com.demo.service.kafka.MessageKafkaSendService;
import com.demo.service.kafka.base.impl.KafkaConfig;
import com.demo.service.kafka.base.impl.KafkaProducerRecordImpl;
import com.demo.service.kafka.base.impl.KafkaChatSendMessageServicesImpl;

@Service
public class MessageKafkaSendServiceImpl extends KafkaChatSendMessageServicesImpl<Message> 
	implements MessageKafkaSendService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageKafkaSendServiceImpl.class);
	
	public final static String _queueName = KafkaConfig.QueueName.MESSAGE_QUEUE;
	
	@PostConstruct
	public void init() throws Exception {
		super.init(_queueName, Message.class);
		// TODO Auto-generated constructor stub
	}
}
