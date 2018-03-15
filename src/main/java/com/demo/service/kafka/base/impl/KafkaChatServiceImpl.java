package com.demo.service.kafka.base.impl;


import org.springframework.beans.factory.annotation.Autowired;

import com.demo.service.kafka.base.KafkaChatService;

public class KafkaChatServiceImpl<T> extends KafkaBaseServiceImpl<T> implements	KafkaChatService<T> {

	
	@Autowired
	KafkaConfig kafkaConfig;
	
	public void init(String topic, Class<T> clazz){
		super.init(KafkaConfig.Config.ZOOKEEPER, KafkaConfig.Config.BOOTSTRAP_LIST, KafkaConfig.Config.GROUP_ID, topic, clazz);
	}
}
