package com.demo.service.kafka.base;

import java.util.List;

public interface KafkaAdminUtils {

	/**
	 * return set Zookeeper url
	 * @return String
	 */
	public String getZookeeper();

	/**
	 * return set Topics
	 * return List<String> Topics
	 */
	public List<String> getTopics();

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
}
