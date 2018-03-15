package com.demo.service.kafka.base.impl;


import com.demo.service.kafka.base.KafkaChatSendMessageServices;

public class KafkaChatSendMessageServicesImpl<T> extends KafkaBaseSendMessageServicesImpl<T>  
	implements KafkaChatSendMessageServices<T> {


	public void init(String topic, Class<T> clazz) {
		super.init(KafkaConfig.Config.ZOOKEEPER, KafkaConfig.Config.BOOTSTRAP_LIST, topic, clazz);
	}	

}
