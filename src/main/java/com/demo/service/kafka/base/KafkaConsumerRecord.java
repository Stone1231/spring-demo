package com.demo.service.kafka.base;

import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;


public interface KafkaConsumerRecord {

	/**
	 * get set Bootstrap list
	 * @return String
	 */
	public String getBootstrapList();

	/**
	 * return set receive group.id
	 * @return String
	 */
	public String getGroupID();
	
	/**
	 * return set Topics
	 * return List<String> Topics
	 */
	public List<String> getTopics();
	
	/**
	 * get set kafka Properties
	 * @return Properties
	 */
	public Properties getProperties();
	
	/**
	 * get topic consumer message list
	 * @param topic
	 * @return message list ConsumerRecord<String, String>
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords(String topic) throws Exception;
	
	/**
	 * get topic consumer message list
	 * @return message list ConsumerRecords<String, String>
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords() throws Exception;
	
	/**
	 * get topic consumer message list
	 * @param topics
	 * @return
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords(List<String> topics) throws Exception;

	/**
	 * receive processes
	 * @param receive listener
	 * @return count
	 * @throws Exception
	 */
	public int receiveMessage(KafkaConsumerReceiveListener listener) throws Exception;

	/**
	 * receive processes
	 * @param topics
	 * @param receive listener
	 * @return count
	 * @throws Exception
	 */
	public int receiveMessage(List<String> topics,	KafkaConsumerReceiveListener listener) throws Exception;

	/**
	 * close receive connect
	 */
	public void closeConnect();
}
