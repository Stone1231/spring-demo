package com.demo.service.kafka.base.impl;


import com.demo.service.kafka.base.KafkaBaseReceiveMessageServices;

public class KafkaBaseReceiveMessageServicesImpl<T> extends KafkaBaseServiceImpl<T> implements
		KafkaBaseReceiveMessageServices<T> {
		

	public void init(String zookeeper, String groupID, String topic, Class<T> clazz) {
		// TODO Auto-generated constructor stub
		this.init(zookeeper, "", groupID, topic, clazz);
	}

	public void init(String zookeeper, String bootstrapList, String groupID, String topic, Class<T> clazz) {
		// TODO Auto-generated constructor stub
		super.init(zookeeper, bootstrapList, groupID, topic, clazz);
	}
}
