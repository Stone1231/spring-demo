package com.demo.service.kafka.base.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.util.Assert;

import com.demo.service.kafka.base.KafkaAdapter;
import com.demo.service.kafka.base.KafkaAdminUtils;
import com.demo.service.kafka.base.KafkaConsumer;
import com.demo.service.kafka.base.KafkaConsumerReceiveListener;
import com.demo.service.kafka.base.KafkaProducer;

import kafka.consumer.KafkaStream;

/**
 * 橋接 Kafka 服務
 * @author Joe.C.Lee
 *
 */
public class KafkaAdapterImpl implements KafkaAdapter {


	public void init(String zookeeper, String bootstrapList, String groupID, String topic){
		init(zookeeper, bootstrapList, groupID, Arrays.asList(topic));
	}
	
	public void init(String zookeeper, String bootstrapList, String groupID, List<String> topics){
		this._zookeeper = zookeeper;
		this._bootstrapList = bootstrapList;
		this._groupID = groupID;
		this._topics = topics;
	}	

	public void init(List<String> topics, Properties properties){
		
		this._topics = topics;
		this._properties = properties;
		
		String propertiesName = "metadata.broker.list";
		if (this._properties.containsKey(propertiesName))
			this._bootstrapList = this._properties.getProperty(propertiesName);		

		propertiesName = "bootstrap.servers";
		if (this._properties.containsKey(propertiesName))
			this._bootstrapList = this._properties.getProperty(propertiesName);		

		propertiesName = "zookeeper.connect";
		if (this._properties.containsKey(propertiesName))
			this._zookeeper = this._properties.getProperty(propertiesName);		
		
		propertiesName = "group.id";
		if (this._properties.containsKey(propertiesName))
			this._groupID = this._properties.getProperty(propertiesName);
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
			_properties = new Properties();   
			_properties.putAll(this.getProducer().getProperties());
			_properties.putAll(this.getConsumer().getProperties());
		}
		
		return _properties;
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

	@Override
	/**
     * delete kafka Topic
	 * @param zookeeper
	 * @param topics name
	 */
	public void deleteTopic(String zookeeper, List<String> topics){  
    	
		getAdminUtils().deleteTopic(zookeeper, topics);    	
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
		
		getAdminUtils().createTopic(zookeeper, topics);    	
    }
	
	private KafkaAdminUtils _adminUtils;
	/**
	 * Kafka Producer
	 * @return Producer
	 */
	protected KafkaAdminUtils getAdminUtils(){	
		if (_adminUtils == null) 
			_adminUtils =  new KafkaAdminUtilsImpl(this.getZookeeper(), this.getTopics());
		return _adminUtils;
	}

	private KafkaProducer _producer;
	/**
	 * Kafka Producer
	 * @return Producer
	 */
	protected KafkaProducer getProducer(){	
		if (_producer == null) 
			_producer =  new KafkaProducerImpl(this.getBootstrapList(), this.getTopics());
		return _producer;
	}

	@Override
	/**
	 * send message
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String message) throws Exception {
		return getProducer().sendMessage(message);
	}

	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String key, String message) throws Exception {
		return getProducer().sendMessage(key, message);
	}

	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topic
	 * @return
	 * @throws Exception
	 */
	public String sendMessage(String key, String message, String topic)
			throws Exception {
		return getProducer().sendMessage(key, message, topic);
	}

	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessage(String key, String message, List<String> topics)
			throws Exception {
		return getProducer().sendMessage(key, message, topics);
	}

	@Override
	/**
	 * send multi message
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(List<String> messages) throws Exception {
		return getProducer().sendMessages(messages);
	}

	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topic
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(String key, List<String> messages)
			throws Exception {
		return getProducer().sendMessages(key, messages);
	}

	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(String key, List<String> messages, String topic)
			throws Exception {
		return getProducer().sendMessages(key, messages, topic);
	}

	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @param topics list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(String key, List<String> messages,
			List<String> topics) throws Exception {
		return getProducer().sendMessages(key, messages, topics);
	}

	
	private KafkaConsumer _consumer;
	/**
	 * Kafka Consumer
	 * @return Consumer
	 */
	protected KafkaConsumer getConsumer(){	
		if (_consumer == null) 
			_consumer =  new KafkaConsumerImpl(this.getZookeeper(), this.getBootstrapList(), this.getGroupID(), this.getTopics());
		return _consumer;
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
		return getConsumer().dataSourceStream(topic);
	}

	@Override
	/**
	 * 0.8.2.0 版 get topics list, consumer message list
	 * @return message list Map<String, List<KafkaStream<byte[], byte[]>>>
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream()
			throws Exception {
		return getConsumer().dataSourceStream();
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
		return getConsumer().dataSourceStream(topics);
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
		return getConsumer().dataSourceRecords(topic);
	}

	@Override
	/**
	 * Consumer 0.9.0.0 版 get topic consumer message list
	 * @return message list ConsumerRecord<String, String>
	 * @throws Exception
	 */
	public ConsumerRecords<String, String> dataSourceRecords() throws Exception {
		return getConsumer().dataSourceRecords();
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
		return getConsumer().dataSourceRecords(topics);
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
		return getConsumer().receiveMessage(listener);
	}

	@Override
	/**
	 * receive processes
	 * @param topics
	 * @param receive listener
	 * @return count
	 * @throws Exception
	 */
	public int receiveMessage(List<String> topics,	KafkaConsumerReceiveListener listener) throws Exception {
		return getConsumer().receiveMessage(topics, listener);
	}

	@Override
	/**
	 * close receive connect
	 */
	public void closeConnect() {
		getConsumer().closeConnect();
	}
}
