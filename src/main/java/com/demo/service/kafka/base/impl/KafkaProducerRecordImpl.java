package com.demo.service.kafka.base.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.demo.service.kafka.base.KafkaProducerRecord;
import com.demo.utils.DateUtil2;

public class KafkaProducerRecordImpl implements KafkaProducerRecord {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerRecordImpl.class);

	public KafkaProducerRecordImpl(String bootstrapList, String topic){
		this(bootstrapList, Arrays.asList(topic));
	}		

	public KafkaProducerRecordImpl(String bootstrapList, List<String> topics){	
		this._bootstrapList = bootstrapList;
		this._topics = topics;
		this.getProperties();
	}     
	
	public KafkaProducerRecordImpl(String topic, Properties properties){
		this(Arrays.asList(topic), properties);
	}	
	
	public KafkaProducerRecordImpl(List<String> topics, Properties properties){
		this._topics = topics;
		this._properties = properties;
		
		String propertiesName = "bootstrap.servers";
		if (this._properties.containsKey(propertiesName))
			this._bootstrapList = this._properties.getProperty(propertiesName);
		
	}

	private String _bootstrapList;
	@Override
	/**
	 * get set Bootstrap list
	 * @return String
	 */
	public String getBootstrapList(){	
    	Assert.notNull(_bootstrapList, "BootstrapList not be null");
		return _bootstrapList;
	}	
	
	private List<String> _topics;
	@Override
	/**
	 * get set Topics
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
		if (_properties == null) _properties = setProperties();
		return _properties;
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
		
        return sendMessages(this.getKey() , messages);
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
		KafkaProducer<String, String> producer = new KafkaProducer<String, String> (this.getProperties());
		
		for(String message:messages){
			Integer partition = 0;
			for(String topic:topics){
				//建立 資料 物件
				ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, partition, key, message);
				LOGGER.info(String.format("topic: %s, key: %s, message: %s", topic, key, message));
				
				//執行 傳送後關閉 端口
				producer.send(data);
				partition++;
			}
		}
				
		producer.close();		
        
        return key;	
    }
	


	/**
	 * 初始化 Properties
	 * @return Properties
	 */
	protected Properties setProperties(){		
		
        Properties props = new Properties();   
        
        props.put("metadata.broker.list", this.getBootstrapList());
        props.put("bootstrap.servers", this.getBootstrapList());
        //props.put("compression.codec", "1");
         props.put("request.required.acks", "1");  
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
     
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
