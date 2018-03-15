package com.demo.service.kafka.base.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.util.Assert;

import com.demo.service.kafka.base.KafkaConsumer;
import com.demo.service.kafka.base.KafkaConsumerReceiveListener;
import com.demo.service.kafka.base.KafkaConsumerRecord;
import com.demo.service.kafka.base.KafkaConsumerStream;

import kafka.consumer.KafkaStream;

public class KafkaConsumerImpl implements KafkaConsumer {


	private final static Version DEFAULT_VERSION = Version.V0820;

	public enum Version {
		V0820, V0900
	}
	
	public KafkaConsumerImpl(String serverlist, String groupID, String topic){
		this(serverlist, groupID, Arrays.asList(topic));
	}	
	
	public KafkaConsumerImpl(String serverlist, String groupID, List<String> topics){
		this(serverlist, groupID, topics, DEFAULT_VERSION);
	}
	
	public KafkaConsumerImpl(String serverlist, String groupID, String topic, Version version){
		this(serverlist, groupID, Arrays.asList(topic), version);
	}	
	
	public KafkaConsumerImpl(String serverlist, String groupID, List<String> topics, Version version){
		this((version==Version.V0820 ? serverlist:""), 
			 (version==Version.V0900 ? "":serverlist), 
			 groupID, 
			 topics, 
			 version);
	}
	
	public KafkaConsumerImpl(String zookeeper, String bootstrapList, String groupID, List<String> topics){
		this(zookeeper, bootstrapList, groupID, topics, DEFAULT_VERSION);
	}
	
	public KafkaConsumerImpl(String zookeeper, String bootstrapList, String groupID, List<String> topics, Version version){
		this._zookeeper = zookeeper;
		this._bootstrapList = bootstrapList;
		this._groupID = groupID;
		this._topics = topics;
		this._version = version;
		this.getProperties();
	}	

	public KafkaConsumerImpl(Properties properties, String topic){
		this(properties, Arrays.asList(topic));
	}	

	public KafkaConsumerImpl(Properties properties, List<String> topics){
		this._topics = topics;
		this._properties = properties;
		
		String propertiesName = "zookeeper.connect";
		if (this._properties.containsKey(propertiesName))
			this._zookeeper = this._properties.getProperty(propertiesName);
		
		propertiesName = "bootstrap.servers";
		if (this._properties.containsKey(propertiesName))
			this._bootstrapList = this._properties.getProperty(propertiesName);
		
		propertiesName = "group.id";
		if (this._properties.containsKey(propertiesName))
			this._groupID = this._properties.getProperty(propertiesName);
	}	

	private Version _version;
	@Override
	/**
	 * Use version
	 * @return Version
	 */
	public Version getVersion(){	
    	Assert.notNull(_version, "Version not be null");
		return _version;
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
	
	private String _bootstrapList;
	@Override
	/**
	 * return set Bootstrap list
	 * @return String
	 */
	public String getBootstrapList(){	
    	Assert.notNull(_bootstrapList, "BootstrapList not be null");
		return _bootstrapList;
	}	
	
	private String _groupID;
	@Override
	/**
	 * return set receive group.id
	 * @return String
	 */
	public String getGroupID(){	
    	Assert.notNull(_groupID, "GroupID not be null");
		return _groupID;
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

	private Properties _properties;
	@Override
	/**
	 * get set kafka Properties
	 * @return Properties
	 */
	public Properties getProperties(){	
		if (_properties == null){ 
			switch(getVersion()){
			case V0820:
				_properties = getStream().getProperties();
				break; 
			case V0900:
				_properties = getRecord().getProperties();
				break;
			}
		}
		return _properties;
	}

	private KafkaConsumerStream _consumerStream;
	/**
	 *  Consumer 0.8.2.0 版
	 * @return Consumer for ConsumerStream
	 */
	public KafkaConsumerStream getStream(){	
		if (_consumerStream == null)
			_consumerStream = new KafkaConsumerStreamImpl(this.getZookeeper(), this.getGroupID(), this.getTopics());
		
		return _consumerStream;
	}

	private KafkaConsumerRecord _consumerRecord;
	/**
	 *  Consumer 0.9.0.0 版
	 * @return Consumer for ConsumerRecord
	 */
	public KafkaConsumerRecord getRecord(){	
//		if (_consumerRecord == null)
//			_consumerRecord = new KafkaConsumerRecordImpl(this.getBootstrapList(), this.getGroupID(), this.getTopics());
		
		return _consumerRecord;
	}
	
	@Override
	/**
	 * 0.8.2.0 版 get topic list, consumer message list
	 * @param topic
	 * @return message list List<KafkaStream<byte[], byte[]>>
	 * @throws Exception
	 */
	public List<KafkaStream<byte[], byte[]>> dataSourceStream(String topic)
			throws Exception {
		// TODO Auto-generated method stub
		return getStream().dataSourceStream(topic);
	}

	@Override
	/**
	 * 0.8.2.0 版 get topics list, consumer message list
	 * @return message list Map<String, List<KafkaStream<byte[], byte[]>>>
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream()
			throws Exception {
		// TODO Auto-generated method stub
		return getStream().dataSourceStream();
	}

	@Override
	/**
	 * 0.8.2.0 版 get topics list, consumer message list
	 * @param topics
	 * @return message list Map<String, List<KafkaStream<byte[], byte[]>>>
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream(
			List<String> topics) throws Exception {
		// TODO Auto-generated method stub
		return getStream().dataSourceStream(topics);
	}

	@Override
	/**
	 * Consumer 0.9.0.0 版 get topic consumer message list
	 * @param topic
	 * @return message list ConsumerRecord<String, String>
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords(
			String topic) throws Exception {
		// TODO Auto-generated method stub
		return getRecord().dataSourceRecords(topic);
	}

	@Override
	/**
	 * Consumer 0.9.0.0 版 get topic consumer message list
	 * @return message list ConsumerRecord<String, String>
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords() throws Exception {
		// TODO Auto-generated method stub
		return getRecord().dataSourceRecords();
	}

	@Override
	/**
	 * Consumer 0.9.0.0 版 get topics consumer message list
	 * @param topics
	 * @return message list ConsumerRecord<String, String>
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords(List<String> topics)
			throws Exception {
		// TODO Auto-generated method stub
		return getRecord().dataSourceRecords(topics);
	}

	@Override
	/**
	 * receive processes
	 * @param receive listener
	 * @return count
	 * @throws Exception
	 */
	public int receiveMessage(KafkaConsumerReceiveListener listener)
			throws Exception {
		// TODO Auto-generated method stub
		int count =0;
		switch(getVersion()){
		case V0820:
			count = getStream().receiveMessage(listener);
			break; 
		case V0900:
			count =  getRecord().receiveMessage(listener);
			break; 
		}		
		
		return count;
	}

	@Override
	/**
	 * receive processes
	 * @param topics
	 * @param receive listener
	 * @return count
	 * @throws Exception
	 */
	public int receiveMessage(List<String> topics,
			KafkaConsumerReceiveListener listener) throws Exception {
		// TODO Auto-generated method stub
		int count =0;
		switch(getVersion()){
		case V0820:
			count = getStream().receiveMessage(topics, listener);
			break; 
		case V0900:
			count = getRecord().receiveMessage(topics, listener);
			break; 
		}		
		return count;
	}

	@Override
	/**
	 * close receive connect
	 */
	public void closeConnect() {
		// TODO Auto-generated method stub
		switch(getVersion()){
		case V0820:
			getStream().closeConnect();
			break; 
		case V0900:
			getRecord().closeConnect();
			break; 
		}		
	}
}
