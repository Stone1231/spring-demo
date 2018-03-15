package com.demo.service.kafka.base;

import java.util.List;

import com.demo.model.Callback;

public interface KafkaBaseSendMessageServices<T> {

	/**
	 * create Topic
	 */
	public void createTopic();
	
	/**
	 * delete kafka Topic
	 */
	public void deleteTopic();
	
	/***
	 * Set flow end callback , for send
	 * @param callback
	 */
	public void setCallback(Callback callback);

	/**
	 * send message
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	//public String sendMessage(T message) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String key, T message) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, String topic) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, List<String> topics) throws Exception;
	
	/**
	 * send multi message
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	//public String sendMessage(List<T> messages) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topic
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, String topic) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, List<String> topics) throws Exception;
	
//============
	/**
	 * send message
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	//public String sendMessage(T message, Callback callback) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, Callback callback) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, String topic, Callback callback) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, T message, List<String> topics, Callback callback) throws Exception;
	
	/**
	 * send multi message
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	//public String sendMessage(List<T> messages, Callback callback) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topic
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, Callback callback) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, String topic, Callback callback) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, List<T> messages, List<String> topics, Callback callback) throws Exception;

}