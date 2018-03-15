package com.demo.service.kafka.base;

import java.util.List;
import java.util.Properties;

public interface KafkaProducerKeyedMessage {

	/**
	 * get set Bootstrap list
	 * @return String
	 */
	public String getBootstrapList();

	/**
	 * get set Topics
	 * return List<String> Topics
	 */
	public List<String> getTopics();
	
	/**
	 * get set kafka Properties
	 * @return
	 */
	public Properties getProperties();	

	/**
	 * send message
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String message) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String key, String message) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String key, String message, String topic) throws Exception;
	
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, String message, List<String> topics) throws Exception;
	
	/**
	 * send multi message
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(List<String> messages) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topic
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(String key, List<String> messages) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(String key, List<String> messages, String topic) throws Exception;
	
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(String key, List<String> messages, List<String> topics) throws Exception;
}
