package com.demo.service.kafka.base;

import com.demo.model.Feeback;

public interface KafkaBaseReceiveListener<T> {
	public Feeback receiveMessage(String key, T message) throws Exception;

}
