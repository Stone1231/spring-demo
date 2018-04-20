package com.demo.service.kafka2;

import com.demo.model.Feeback;

public interface ReceiveListener<T> {
	Class<T> getClazz();
	
	Feeback receiveMessage(String key, T message) throws Exception;
}
