package com.demo.service.kafka.base.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.common.MessageStreamsExistException;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.TopicFilter;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.demo.model.Feeback;
import com.demo.service.kafka.Impl.MessageKafkaReceiveServiceImpl;
import com.demo.service.kafka.base.KafkaConsumerReceiveListener;
import com.demo.service.kafka.base.KafkaConsumerStream;
import com.demo.utils.StringUtil;

public class KafkaConsumerStreamImpl implements KafkaConsumerStream{

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerStreamImpl.class);
	
	public KafkaConsumerStreamImpl(String zookeeper, String groupID, String topic){
		this(zookeeper, groupID, Arrays.asList(topic));
	}	

	public KafkaConsumerStreamImpl(String zookeeper, String groupID, String topic, OffsetResetStrategy offsetReset){
		this(zookeeper, groupID, Arrays.asList(topic));
	}	

	public KafkaConsumerStreamImpl(String zookeeper, String groupID, List<String> topics){
		this(zookeeper, groupID, topics, OffsetResetStrategy.smallest);
	}	
	
	public KafkaConsumerStreamImpl(String zookeeper, String groupID, List<String> topics, OffsetResetStrategy offsetReset){
		this._zookeeper = zookeeper;
		this._groupID = groupID;
		this._topics = topics;
		this._offsetReset = offsetReset;
		this.getProperties();
	}	

	public KafkaConsumerStreamImpl(Properties properties, String topic){
		this(properties, Arrays.asList(topic));
	}	

	public KafkaConsumerStreamImpl(Properties properties, List<String> topics){
		this._topics = topics;
		this._properties = properties;
		
		String propertiesName = "zookeeper.connect";
		if (this._properties.containsKey(propertiesName))
			this._zookeeper = this._properties.getProperty(propertiesName);		
		
		propertiesName = "group.id";
		if (this._properties.containsKey(propertiesName))
			this._groupID = this._properties.getProperty(propertiesName);
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
		if (_properties == null) _properties = setProperties();
		return _properties;
	}	
	
	@Override
	/**
	 * get topics list, consumer message list
	 * @param topics
	 * @return
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream(List<String> topics) throws Exception{
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap;
		
		int topicsCount = 1;
		for(String topic:topics){
			LOGGER.info(String.format("kafka:topicsCount = %s", topicsCount));
			topicCountMap.put(topic, new Integer(topicsCount++));
			                         //代表這個VM要開幾條通道 
		}
		
		try{			
			consumerMap = consumer().createMessageStreams(topicCountMap);
			LOGGER.info("kafka:createMessageStreams");
		} catch (MessageStreamsExistException me){
			consumerMap = new HashMap<String, List<KafkaStream<byte[],byte[]>>>();
			LOGGER.info("kafka:createMessageStreams again");
			for(String topic:topics){
				TopicFilter filter = new Whitelist(topic);
				consumerMap.put(topic, consumer().createMessageStreamsByFilter(filter));
			}			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("receiveTopicMessages",e);
			throw e; 
		} finally{
			//closeConnect();
			//LOGGER.info("kafka:closeConnect");
			LOGGER.info("kafka:finally");
		}		
        return consumerMap;
	}	

	@Override
	/**
	 * get topic consumer message list
	 * @return message list KafkaStream<byte[], byte[]>
	 * @throws Exception
	 */
	public List<KafkaStream<byte[], byte[]>> dataSourceStream(String topic) throws Exception{
		LOGGER.info(String.format("Receive Topic:%s", topic));
		return dataSourceStream(Arrays.asList(topic)).get(topic);
	}

	@Override
	/**
	 * get topics list, consumer message list
	 * @param topic
	 * @return message list KafkaStream<byte[], byte[]>
	 * @throws Exception
	 */
	public Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream() throws Exception {
		// TODO Auto-generated method stub
		return dataSourceStream(this.getTopics());
	}

	@Override
	/**
	 * receive processes
	 * @param receive listener
	 * @return count
	 * @throws Exception
	 */
	public int receiveMessage(KafkaConsumerReceiveListener listener) throws Exception{
		return receiveMessage(this.getTopics(), listener);
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
		// TODO Auto-generated method stub
		int count = 0;
		Map<String, List<KafkaStream<byte[], byte[]>>> dataSource = dataSourceStream(topics);
		LOGGER.info("kafka:dataSourceStream");
		for(String topic : topics) {
			List<KafkaStream<byte[], byte[]>> topicsMessages = dataSource.get(topic);
			for (KafkaStream<byte[], byte[]> topicMessages : topicsMessages) {
				count = count + receiveMessage(topicMessages, listener);
			}
		}
		
		return count;
	}	

	/**
	 * receive message loop, error rollback
	 * @param stream
	 * @param listener
	 * @throws Exception
	 */
	private int receiveMessage(KafkaStream<byte[], byte[]> records, KafkaConsumerReceiveListener listener) throws Exception{
		
		int count = 0;
    	//Map<TopicAndPartition, OffsetAndMetadata> offsets = new HashMap<TopicAndPartition, OffsetAndMetadata>();
		ConsumerIterator<byte[], byte[]> consumerIterator = records.iterator();
		while (consumerIterator.hasNext()){
    	//for (MessageAndMetadata<byte[], byte[]> record : records){
			MessageAndMetadata<byte[], byte[]> record = consumerIterator.next();
        	String topic = record.topic();
        	String key = transNull2EmptyStr(record.key());
        	String message = transNull2EmptyStr(record.message());        	
        	Feeback metadata = null;
        	
        	try{
        		_consumer.commitOffsets(true);
        		metadata = receiveMessage(key, message, listener);
        	} catch (Exception e) {
        		//錯誤還原
        		metadata = new Feeback(e);
        		_consumer.commitOffsets(false);
    			LOGGER.error(String.format("[%s] rollback (key[%s]:%s)", topic, record.offset(), key, e));
        	}finally{      		
        		count++;
        	} 
    	}  
    	
    	return count;
	}
	
	/**
	 * transform byte[] to String and check Empty
	 * @param str byte[]
	 * @return String
	 */
	private String transNull2EmptyStr(byte[] str) {		
		if (StringUtil.isNullOrEmpty(str)) {
			return "";
		}
		return new String(str);
	} 
	
	/**
	 * use Listener
	 * @param key
	 * @param message
	 * @param listener
	 * @return Feeback
	 * @throws Exception
	 */
	private Feeback receiveMessage(String key, String message, KafkaConsumerReceiveListener listener) throws Exception{
    	LOGGER.info(String.format("key:%s \n message:%s\n", key, message));
		if (listener != null) 
			return listener.receiveMessage(key, message);
		else{
			return new Feeback("");
		}
	}

	@Override
	/**
	 * close receive connect
	 */
	public void closeConnect() {
		// TODO Auto-generated method stub
		if (_consumer != null) _consumer.shutdown();
	}

	private ConsumerConnector _consumer;
	/**
	 * consumer Connect
	 * @return ConsumerConnector
	 */
	protected ConsumerConnector consumer(){	
		if (_consumer == null) 
			_consumer =  Consumer.createJavaConsumerConnector(new ConsumerConfig(getProperties()));
		return _consumer;
	}

	private OffsetResetStrategy _offsetReset = OffsetResetStrategy.smallest;
	/**
	 * OffsetReset
	 * @return OffsetReset
	 */
	protected OffsetResetStrategy getOffsetReset(){	
		return _offsetReset;
	}	

	/**
	 * set Properties
	 * @return Properties
	 */
	private Properties setProperties(){
		return setProperties(getOffsetReset());
	}
	
	/**
	 * set Properties for OffsetReset
	 * @return Properties
	 */
	private Properties setProperties(OffsetResetStrategy offsetReset){     
        
        Properties props = new Properties();   
        
        props.put("zookeeper.connect", getZookeeper());        
        props.put("group.id", getGroupID());
        
        //設定 與 zookeeper的連線的 timeout 時間
        props.put("zookeeper.session.timeout.ms", "30000");
        //設定 ZooKeeper集群中l eader和follower的同步時間
        props.put("zookeeper.sync.time.ms", "1000");
        
        props.put("auto.commit.enable", "false");
       
        //自动提交 offset 的時間間隔
        props.put("auto.commit.interval.ms", "1000");        
        props.put("auto.offset.reset", offsetReset.name().replace("_", " "));
        props.put("rebalance.max.retries", "5");  
        props.put("rebalance.backoff.ms", "30000");
        props.put("request.required.acks", "-1");        
        
        return props; 
    }
	
	public enum OffsetResetStrategy {
		largest, smallest, anything_else
	}

}
