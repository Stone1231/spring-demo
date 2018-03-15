package com.demo.service.kafka.base.impl;


import com.demo.service.kafka.base.KafkaBaseSendMessageServices;

public class KafkaBaseSendMessageServicesImpl<T> extends KafkaBaseServiceImpl<T> implements
		KafkaBaseSendMessageServices<T> {


	public void init(String zookeeper, String bootstrapList, String topic, Class<T> clazz) {
		// TODO Auto-generated constructor stub
		super.init(zookeeper, bootstrapList, "", topic, clazz);
	}	
}
