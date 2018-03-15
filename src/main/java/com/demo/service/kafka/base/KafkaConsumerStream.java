package com.demo.service.kafka.base;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.KafkaStream;

public interface KafkaConsumerStream {

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
	 * get topic consumer message list
	 * @return message list KafkaStream<byte[], byte[]>
	 * @throws Exception
	 */
	public List<KafkaStream<byte[], byte[]>> dataSourceStream(String topic) throws Exception;
	
	/**
	 * get topics consumer message list
	 * @return message list KafkaStream<byte[], byte[]>
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream() throws Exception;
	
	/**
	 * get topics consumer message list
	 * @param topics
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream(List<String> topics) throws Exception;


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