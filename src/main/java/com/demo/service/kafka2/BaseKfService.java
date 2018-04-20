package com.demo.service.kafka2;

import java.util.List;

public interface BaseKfService <T> {
	void send(String topic, String key, T msg);

	void send(List<String> topics, String key, T msg);

	void receiver(String topic, ReceiveListener<T> listener);
	
	void receiver(List<String> topics, ReceiveListener<T> listener);
}
