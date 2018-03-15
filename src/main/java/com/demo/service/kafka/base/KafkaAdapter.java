package com.demo.service.kafka.base;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import kafka.consumer.KafkaStream;

//import org.apache.kafka.clients.producer.Callback;

public interface KafkaAdapter {

	/**
	 * return set Bootstrap list
	 * @return String
	 */
	public String getBootstrapList();
	
	/**
	 * return set Zookeeper url
	 * @return String
	 */
	public String getZookeeper();
	
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
	 * create Topic
	 */
	public void createTopic();
	
	/**
	 * create Topic
	 * @param zookeeper
	 */
	public void createTopic(String zookeeper);

	/**
	 * create Topic
	 * @param zookeeper
	 * @param topic
	 */
	public void createTopic(String zookeeper, List<String> topics);      

	/**
	 * delete kafka Topic
	 */
	public void deleteTopic();
	
	/**
     * delete kafka Topic
     * @param zookeeper
     */
	public void deleteTopic(String zookeeper);
	
	/**
     * delete kafka Topic
	 * @param zookeeper
	 * @param topics name
	 */
	public void deleteTopic(String zookeeper, List<String> topics);

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
	
	/**
	 * 0.8.2.0 版
	 * get topic consumer message list
	 * @return message list KafkaStream<byte[], byte[]>
	 * @throws Exception
	 */
	public List<KafkaStream<byte[], byte[]>> dataSourceStream(String topic) throws Exception;
	
	/**
	 * 0.8.2.0 版
	 * get topics consumer message list
	 * @return message list KafkaStream<byte[], byte[]>
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream() throws Exception;
	
	/**
	 * 0.8.2.0 版
	 * get topics consumer message list
	 * @param topics
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream(List<String> topics) throws Exception;

	/**
	 * 0.9.0.0 版
	 * get topic consumer message list
	 * @param topic
	 * @return message list ConsumerRecord<String, String>
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords(String topic) throws Exception;
	
	/**
	 * 0.9.0.0 版
	 * get topic consumer message list
	 * @return message list ConsumerRecords<String, String>
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords() throws Exception;
	
	/**
	 * 0.9.0.0 版
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
