package com.demo.service.kafka.base.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.util.Assert;

import com.demo.service.kafka.base.KafkaProducer;
import com.demo.service.kafka.base.KafkaProducerKeyedMessage;
import com.demo.service.kafka.base.KafkaProducerRecord;

public class KafkaProducerImpl implements KafkaProducer {
	
	
	private final static Version DEFAULT_VERSION = Version.V0820;

	public enum Version {
		V0820, V0900
	}
	
	public KafkaProducerImpl(String bootstrapList, String topic){
		this(bootstrapList, topic, DEFAULT_VERSION);
	}	
	
	public KafkaProducerImpl(String bootstrapList, String topic, Version version){	
		this(bootstrapList, Arrays.asList(topic), version);
	}	

	public KafkaProducerImpl(String bootstrapList, List<String> topics){
		this(bootstrapList, topics, DEFAULT_VERSION);
	}	

	public KafkaProducerImpl(String bootstrapList, List<String> topics, Version version){	
		this._bootstrapList = bootstrapList;
		this._topics = topics;
		this._version = version;
		this.getProperties();
	}     
	
	public KafkaProducerImpl(String topic, Properties properties){
		this(Arrays.asList(topic), properties);
	}	

	public KafkaProducerImpl(List<String> topics, Properties properties){
		this(topics, properties, DEFAULT_VERSION);
	}	
	
	public KafkaProducerImpl(List<String> topics, Properties properties, Version version){
		this._topics = topics;
		this._properties = properties;
		
 		//"bootstrap.servers"
		String propertiesName = "bootstrap.servers";
		if (this._properties.containsKey(propertiesName))
			this._bootstrapList = this._properties.getProperty(propertiesName);
		
		propertiesName = "metadata.broker.list";
		if (this._properties.containsKey(propertiesName))
			this._bootstrapList = this._properties.getProperty(propertiesName);	

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

	
	private String _bootstrapList;
	@Override
	/**
	 * return set Bootstrap list
	 * @return String
	 */
	public String getBootstrapList(){	
    	Assert.hasText(_bootstrapList, "BootstrapList not be null");
		return _bootstrapList;
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
				_properties = getKeyedMessage().getProperties();
				break; 
			case V0900:
				_properties = getRecord().getProperties();
				break;
			}
		}
		return _properties;
	}
	
	private KafkaProducerKeyedMessage _producerKeyedMessage;
	/**
	 *  Producer 0.8.2.0 版
	 * @return producer for KeyedMessage
	 */
	protected KafkaProducerKeyedMessage getKeyedMessage(){	
		if (_producerKeyedMessage == null)
			
			_producerKeyedMessage =  new KafkaProducerKeyedMessageImpl(this.getBootstrapList(), this.getTopics());
		return _producerKeyedMessage;
	}
	
	private KafkaProducerRecord _producerRecord;
	/**
	 *  Producer 0.9.0.0 版
	 * @return producer for ProducerRecord
	 */
	protected KafkaProducerRecord getRecord(){	
		if (_producerRecord == null) 
			_producerRecord =  new KafkaProducerRecordImpl(this.getBootstrapList(), this.getTopics());
		return _producerRecord;
	}

	@Override
	/**
	 * send message
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String message) throws Exception {
		// TODO Auto-generated method stub
		String key = "";
		switch(getVersion()){
		case V0820:
			key = getKeyedMessage().sendMessage(message);
			break; 
		case V0900:
			key = getRecord().sendMessage(message);
			break; 
		}
		return key;
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
		// TODO Auto-generated method stub
		switch(getVersion()){
		case V0820:
			key = getKeyedMessage().sendMessage(key, message);
			break; 
		case V0900:
			key = getRecord().sendMessage(key, message);
			break; 
		}
		return key;
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
		// TODO Auto-generated method stub
		switch(getVersion()){
		case V0820:
			key = getKeyedMessage().sendMessage(key, message);
			break; 
		case V0900:
			key = getRecord().sendMessage(key, message);
			break; 
		}
		return key;
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
		// TODO Auto-generated method stub
		switch(getVersion()){
		case V0820:
			key =  getKeyedMessage().sendMessage(key, message, topics);
			break; 
		case V0900:
			key =  getRecord().sendMessage(key, message, topics);
			break; 
		}
		return key;
	}

	@Override
	/**
	 * send multi message
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(List<String> messages) throws Exception {
		// TODO Auto-generated method stub
		String key="";
		switch(getVersion()){
		case V0820:
			key = getKeyedMessage().sendMessages(messages);
			break; 
		case V0900:
			key = getRecord().sendMessages(messages);
			break; 
		}
		return key;
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
		// TODO Auto-generated method stub
		switch(getVersion()){
		case V0820:
			key = getKeyedMessage().sendMessages(key, messages);
			break; 
		case V0900:
			key = getRecord().sendMessages(key, messages);
			break; 
		}
		return key;
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
		// TODO Auto-generated method stub
		switch(getVersion()){
		case V0820:
			key = getKeyedMessage().sendMessages(key, messages, topic);
			break; 
		case V0900:
			key = getRecord().sendMessages(key, messages, topic);
			break; 
		}
		return key;
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
		// TODO Auto-generated method stub
		switch(getVersion()){
		case V0820:
			key =  getKeyedMessage().sendMessages(key, messages, topics);
			break; 
		case V0900:
			key =  getRecord().sendMessages(key, messages, topics);
			break; 
		}
		return key;
	}	
	
}
