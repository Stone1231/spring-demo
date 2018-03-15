package com.demo.service.kafka.base;

import org.springframework.context.ApplicationContext;

import com.demo.model.Callback;

public interface KafkaBaseService<T> extends KafkaBaseSendMessageServices<T>, KafkaBaseReceiveMessageServices<T> {

	/***
	 * Set flow end callback
	 * @param ApplicationContext (sprint use)
	 * @param callback
	 */
	public void setCallback(ApplicationContext appContext, Callback callback);
}
