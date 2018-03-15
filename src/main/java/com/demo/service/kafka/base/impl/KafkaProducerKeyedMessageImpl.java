package com.demo.service.kafka.base.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

//import org.apache.http.annotation.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.demo.service.kafka.base.KafkaProducerKeyedMessage;
import com.demo.utils.DateUtil2;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

//@ThreadSafe
public class KafkaProducerKeyedMessageImpl implements KafkaProducerKeyedMessage{

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerKeyedMessageImpl.class);
	
	private static Producer<String, String> producer;
	public KafkaProducerKeyedMessageImpl(String bootstrapList, String topic){
		this(bootstrapList, Arrays.asList(topic));
	}		
	
	public KafkaProducerKeyedMessageImpl(String bootstrapList, List<String> topics){	
		this.bootstrapList = bootstrapList;
		this.topics = topics;
		this.getProperties();
	}     
	
	public KafkaProducerKeyedMessageImpl(String topic, Properties properties){
		this(Arrays.asList(topic), properties);
	}
	
	public KafkaProducerKeyedMessageImpl(List<String> topics, Properties properties){
		this.topics = topics;
		this.properties = properties;
		
		String propertiesName = "metadata.broker.list";
		if (this.properties.containsKey(propertiesName))
			this.bootstrapList = this.properties.getProperty(propertiesName);		
	}
	
	public synchronized static Producer<String, String> getProducer(Properties getProperties){
        if(producer == null){
        	producer = new Producer<>(new ProducerConfig(getProperties));
        } 
        return producer;
    }

	private String bootstrapList;
	@Override
	/**
	 * get set Bootstrap list
	 * @return String
	 */
	public String getBootstrapList(){	
    	Assert.notNull(bootstrapList, "BootstrapList not be null");
		return bootstrapList;
	}	
	
	private List<String> topics;
	@Override
	/**
	 * get set Topics
	 * return List<String> Topics
	 */
	public List<String> getTopics(){	
    	Assert.notNull(topics, "Topics not be null");
		return topics;
	}	

	private Properties properties;
	@Override
	/**
	 * get set kafka Properties
	 * @return Properties
	 */
	public Properties getProperties(){	
		if (properties == null) properties = setProperties();
		return properties;
	}	

	@Override
	/**
	 * send message
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String message) throws Exception{
		
		return sendMessage(this.getKey() , message);
	}		

	@Override
	/**
	 * send message
	 * @param key
	 * @param message
	 * @return key String
	 * @throws Exception
	 */
	public String sendMessage(String key, String message) throws Exception{
		
		return sendMessages(key, Arrays.asList(message));
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
	public String sendMessage(String key, String message, String topic) throws Exception{
		
		return sendMessage(key, message, Arrays.asList(topic));
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
	public String sendMessage(String key, String message, List<String> topics) throws Exception{
		
		return sendMessages(key, Arrays.asList(message), topics);

	}
	
	@Override
	/**
	 * send multi message
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(List<String> messages) throws Exception{
		
        return sendMessages(this.getKey(), messages);
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
	public String sendMessages(String key, List<String> messages) throws Exception{
        return sendMessages(key, messages, this.getTopics());
	}

	@Override
	/**
	 * send multi message
	 * @param key
	 * @param messages list
	 * @return key
	 * @throws Exception
	 */
	public String sendMessages(String key, List<String> messages, String topic) throws Exception{
		
		return sendMessages(key, messages, Arrays.asList(topic));
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
	public String sendMessages(String key, List<String> messages, List<String> topics) throws Exception{
		
	  //建立 Producer 物件
	    producer = getProducer(getProperties());
		for(String message:messages) {
			for(String topic:topics) {
				//建立 資料 物件
				KeyedMessage<String, String> data = new KeyedMessage<>(topic, key, message);
				LOGGER.info(String.format("topic: %s, key: %s, message: %s", topic, key, message));
				
				//執行 傳送後關閉 端口
				producer.send(data);
			}
		}
        return key;	
    }	

	/**
	 * 初始化 Properties
	 * @return Properties
	 */
	protected Properties setProperties(){		
		
        Properties props = new Properties();          
        props.put("metadata.broker.list", this.getBootstrapList());
		props.put("serializer.class", StringEncoder.class.getName());
        props.put("producer.type", "async");
        props.put("request.required.acks", "1");  
        
        return props; 
	}
	
	/**
	 * 預設 Key
	 * @return String
	 */
	protected String getKey(){
		Random rnd = new Random();
		return String.format("%s%s",
				DateUtil2.nowUTCTimestamp(),
				String.format("%03d",rnd.nextInt(255)));		
	}

}
