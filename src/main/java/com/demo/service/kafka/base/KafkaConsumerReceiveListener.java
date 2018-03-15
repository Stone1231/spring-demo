package com.demo.service.kafka.base;

import com.demo.model.Feeback;

public interface KafkaConsumerReceiveListener {
	public Feeback receiveMessage(String key, String message) throws Exception;
}
