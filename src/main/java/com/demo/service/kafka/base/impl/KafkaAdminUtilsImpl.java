package com.demo.service.kafka.base.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.util.Assert;

import com.demo.service.kafka.base.KafkaAdminUtils;

import kafka.admin.AdminUtils;
import kafka.utils.ZKStringSerializer$;

public class KafkaAdminUtilsImpl implements KafkaAdminUtils{

	public KafkaAdminUtilsImpl(String topics) {
		this(Arrays.asList(topics));
	}

	public KafkaAdminUtilsImpl(String zookeeper, String topics) {
		this(zookeeper, Arrays.asList(topics));
	}

	public KafkaAdminUtilsImpl(List<String> topics) {
		this._topics = topics;
	}
	
	public KafkaAdminUtilsImpl(String zookeeper, List<String> topics) {
		this._zookeeper = zookeeper;
		this._topics = topics;
	}
	
	private String _zookeeper;
	@Override
	/**
	 * return set Zookeeper url
	 * @return String
	 */
	public String getZookeeper(){	
    	Assert.notNull(_zookeeper, "Zookeeper not be null");
		return _zookeeper;
	}	

	private List<String> _topics;
	@Override
	/**
	 * return set Topics
	 * return List<String> Topics
	 */
	public List<String> getTopics(){
    	Assert.notNull(_topics, "Topics not be null");
		return _topics;
	}	

	@Override
	/**
	 * delete kafka Topic
	 */
    public void deleteTopic(){ 
    	deleteTopic(getZookeeper(), getTopics());
    }
    
	@Override
	/**
     * delete kafka Topic
     * @param zookeeper
     */
	public void deleteTopic(String zookeeper){ 
    	deleteTopic(zookeeper, getTopics());
    }

	
	//===================================0.8.x.x=================
	@Override
	/**
     * delete kafka Topic
	 * @param zookeeper
	 * @param topics name
	 */
	public void deleteTopic(String zookeeper, List<String> topics){  
    	
    	Assert.notNull(zookeeper, "zookeeper not be null");
    	ZkClient zkClient = new ZkClient(zookeeper.trim(), 60000, 60000, ZKStringSerializer$.MODULE$);
		for(String topic:topics){
			deleteTopic(zkClient, topic);
		}
		
		zkClient.close();    	
    }
    
	/**
     * delete kafka Topic
	 * @param zkUtils
	 * @param topic
	 */
	private void deleteTopic(ZkClient zkClient, String topic){
		if(AdminUtils.topicExists(zkClient, topic)){
			//String topic, int partitionCount, int replicationFactor
	    	AdminUtils.deleteTopic(zkClient, topic);
		}
	}

	@Override
	/**
	 * create Topic
	 */
    public void createTopic(){ 
    	createTopic(this.getZookeeper(), getTopics());
    }
    
	@Override
	/**
	 * create Topic
	 * @param zookeeper
	 */
	public void createTopic(String zookeeper){ 
    	createTopic(zookeeper, getTopics());
    }
	
	@Override
	/**
	 * create Topic
	 * @param zookeeper
	 * @param topic
	 */
	public void createTopic(String zookeeper, List<String> topics){        

    	Assert.notNull(zookeeper, "zookeeper not be null");
    	ZkClient zkClient = new ZkClient(zookeeper.trim(), 60000, 60000, ZKStringSerializer$.MODULE$);
		for(String topic:topics){
			createTopic(zkClient, topic);
		}
		
		zkClient.close();
    }

	/**
	 * create Topic
	 * @param zkUtils
	 * @param topic
	 */
	private void createTopic(ZkClient zkClient, String topic){
		
		if(!AdminUtils.topicExists(zkClient, topic)){
			//String topic, int partitionCount, int replicationFactor
			AdminUtils.createTopic(zkClient, topic, 10, 1, new Properties());
		}
	}
}
