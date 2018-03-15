package com.demo.service.kafka.base.impl;


import org.springframework.beans.factory.annotation.Autowired;

import com.demo.service.kafka.base.KafkaChatReceiveMessageServices;

public class KafkaChatReceiveMessageServicesImpl<T> extends KafkaBaseReceiveMessageServicesImpl<T> 
	implements KafkaChatReceiveMessageServices<T> {

	
	@Autowired
	KafkaConfig kafkaConfig;

	public void init(String topic, Class<T> clazz) {
		if(!KafkaConfig.Config.ENABLE){
			return;
		}
		super.init(KafkaConfig.Config.ZOOKEEPER, KafkaConfig.Config.BOOTSTRAP_LIST, KafkaConfig.Config.GROUP_ID, topic, clazz);
	}	
}
