package com.demo.service.kafka.Impl;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.demo.model.Message;
import com.demo.service.kafka.MessageKafkaReceiveService;
import com.demo.service.kafka.base.impl.KafkaConfig;
import com.demo.service.mqtt.impl.MqttPubServiceImpl;
import com.demo.service.kafka.base.impl.KafkaChatReceiveMessageServicesImpl;

@Service
public class MessageKafkaReceiveServiceImpl extends KafkaChatReceiveMessageServicesImpl<Message> 
	implements	MessageKafkaReceiveService {

	@Autowired
	private ApplicationContext _appContext;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageKafkaReceiveServiceImpl.class);
	
	public final static String _queueName = KafkaConfig.QueueName.MESSAGE_QUEUE;
	
	
	@PostConstruct
	public void init() {
		super.init(_queueName, Message.class);
		super.setAppContext(_appContext);
	}	
}
