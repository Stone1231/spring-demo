package com.demo.service.kafka.base;

import com.demo.model.Feeback;

public interface KafkaCallbackListener<T> {
	
	public void onCompletion(String key, T message, Feeback feedback, Exception exception);

}
