//package com.demo.service.kafka.base.impl;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
//import org.apache.kafka.clients.consumer.OffsetAndMetadata;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.clients.consumer.OffsetResetStrategy;
//import org.apache.kafka.common.TopicPartition;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.util.Assert;
//
//import com.demo.model.Feeback;
//import com.demo.model.Feeback.Status;
//import com.demo.service.kafka.base.KafkaConsumerReceiveListener;
//import com.demo.service.kafka.base.KafkaConsumerRecord;
//import com.demo.utils.StringUtil;
//
//public class KafkaConsumerRecordImpl implements KafkaConsumerRecord {
//
//	private static transient final Logger LOGGER = LogManager.getLogger(KafkaConsumerRecordImpl.class);
//	private final int TIMEOUT = 100;
//
//	public KafkaConsumerRecordImpl(String bootstrapList, String groupID, String topic){
//		this(bootstrapList, groupID, Arrays.asList(topic));
//	}	
//
//	public KafkaConsumerRecordImpl(String bootstrapList, String groupID, String topic, OffsetResetStrategy offsetReset){
//		this(bootstrapList, groupID, Arrays.asList(topic), offsetReset);
//	}	
//
//	public KafkaConsumerRecordImpl(String bootstrapList, String groupID, List<String> topics){
//		this(bootstrapList, groupID, topics, OffsetResetStrategy.LATEST);
//	}	
//	
//	public KafkaConsumerRecordImpl(String bootstrapList, String groupID, List<String> topics, OffsetResetStrategy offsetReset){
//		this._bootstrapList = bootstrapList;
//		this._groupID = groupID;
//		this._topics = topics;
//		this._offsetReset = offsetReset;
//		this.getProperties();
//	}	
//
//	public KafkaConsumerRecordImpl(Properties properties, String topic){
//		this(properties, Arrays.asList(topic));
//	}	
//
//	public KafkaConsumerRecordImpl(Properties properties, List<String> topics){
//		this._topics = topics;
//		this._properties = properties;
//				
//		String propertiesName = "bootstrap.servers";
//		if (this._properties.containsKey(propertiesName))
//			this._bootstrapList = this._properties.getProperty(propertiesName);
//		
//		propertiesName = "group.id";
//		if (this._properties.containsKey(propertiesName))
//			this._groupID = this._properties.getProperty(propertiesName);
//	}	
//
//	private String _bootstrapList;
//	@Override
//	/**
//	 * get set Bootstrap list
//	 * @return String
//	 */
//	public String getBootstrapList(){
//    	Assert.hasText(_bootstrapList, "Bootstrap not be null");
//		return _bootstrapList;
//	}	
//	
//	private String _groupID;
//	@Override
//	/**
//	 * return set receive group.id
//	 * @return String
//	 */
//	public String getGroupID(){	
//    	Assert.hasText(_groupID, "GroupID not be null");
//		return _groupID;
//	}	
//
//	
//	private List<String> _topics;
//	@Override
//	/**
//	 * return set Topics
//	 * return List<String> Topics
//	 */
//	public List<String> getTopics(){	
//    	Assert.notNull(_topics, "Topics not be null");
//		return _topics;
//	}	
//
//	private Properties _properties;
//	@Override
//	/**
//	 * get set kafka Properties
//	 * @return Properties
//	 */
//	public Properties getProperties(){	
//		if (_properties == null) _properties = setProperties();
//		return _properties;
//	}	
//	
//	private OffsetResetStrategy _offsetReset = OffsetResetStrategy.LATEST;
//	/**
//	 * OffsetReset
//	 * @return OffsetReset
//	 */
//	protected OffsetResetStrategy getOffsetReset(){	
//		return _offsetReset;
//	}	
//
//
//
//	@Override
//	/**
//	 * get topic consumer message list
//	 * @param topic
//	 * @return message list Iterable<ConsumerRecord<String, String>>
//	 * @throws Exception
//	 */
//	public ConsumerRecords<String, String> dataSourceRecords(String topic)
//			throws Exception {
//		// TODO Auto-generated method stub
//		return dataSourceRecords(Arrays.asList(topic));
//	}
//
//	@Override
//	/**
//	 * get topics list, consumer message list
//	 * @return message list ConsumerRecords<String, String>
//	 * @throws Exception
//	 */
//	public ConsumerRecords<String, String> dataSourceRecords() throws Exception {
//		// TODO Auto-generated method stub
//		return dataSourceRecords(this.getTopics());
//	}
//
//	@Override
//	/**
//	 * get topics list, consumer message list
//	 * @param topics
//	 * @return 
//	 * @throws Exception
//	 */
//	public ConsumerRecords<String, String> dataSourceRecords(List<String> topics)
//			throws Exception {
//		// TODO Auto-generated method stub
//		this.consumer().subscribe(topics);
//		return this.consumer().poll(TIMEOUT);
//	}
//
//	@Override
//	/**
//	 * receive processes
//	 * @param receive listener
//	 * @return count
//	 * @throws Exception
//	 */
//	public int receiveMessage(KafkaConsumerReceiveListener listener) throws Exception{
//		// TODO Auto-generated method stub
//		return receiveMessage(this.getTopics(), listener);
//	}	
//
//	@Override
//	/**
//	 * receive processes
//	 * @param topics
//	 * @param receive listener
//	 * @return count
//	 * @throws Exception
//	 */
//	public int receiveMessage(List<String> topics,
//			KafkaConsumerReceiveListener listener) throws Exception {
//		// TODO Auto-generated method stub
//		int count = 0;
//		ConsumerRecords<String, String> dataSource = dataSourceRecords(topics);
//		if (!dataSource.isEmpty()){
//			count = count + receiveMessage(dataSource, listener);
//		}
//		
//		return count;
//	}
//	
//	/**
//	 * receive message loop, error rollback
//	 * @param stream
//	 * @param listener
//	 * @return count
//	 * @throws Exception
//	 */
//	private int receiveMessage(ConsumerRecords<String, String> records, KafkaConsumerReceiveListener listener) throws Exception{
//
//		Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<TopicPartition, OffsetAndMetadata>();
//		
//		int count = 0;
//		for (ConsumerRecord<String, String> record : records) {
//        	String topic = record.topic();
//        	String key = transNull2EmptyStr(record.key());
//        	String message = transNull2EmptyStr(record.value());        	
//        	Feeback metadata = null;
//        	
//        	try{
//        		metadata = receiveMessage(key, message, listener);
//        	} catch (Exception e) {
//        		//錯誤還原
//        		metadata = new Feeback(e);
//    			LOGGER.error(String.format("[%s] rollback (key[%s]:%s)", topic, record.offset(), key, e));
//        	}finally{
//        		offsets.put(new TopicPartition(topic, record.partition()), 
//            			new OffsetAndMetadata(record.offset(), metadata.toString()));
//	   			if (metadata.getStatus()== Status.success) 
//	   				_consumer.commitSync(offsets);
//	   			offsets.clear();
//	   			
//	   			count ++;
//        	} 			
//		}
//		
//		return count;
//	}	
//	
//	/**
//	 * use Listener
//	 * @param key
//	 * @param message
//	 * @param listener
//	 * @return Feeback
//	 * @throws Exception
//	 */
//	private Feeback receiveMessage(String key, String message, KafkaConsumerReceiveListener listener) throws Exception{
//    	LOGGER.info(String.format("key:%s \n message:%s\n", key, message));
//		if (listener != null) 
//			return listener.receiveMessage(key, message);
//		else{
//			return new Feeback("");
//		}
//	}
//
//	/**
//	 * transform String to String and check Empty
//	 * @param str String
//	 * @return String
//	 */
//	private String transNull2EmptyStr(String str) {		
//		if (StringUtil.isNullOrEmpty(str)) {
//			return "";
//		}
//		return str;
//	} 
//	
//	@Override
//	/**
//	 * close receive connect
//	 */
//	public void closeConnect() {
//		// TODO Auto-generated method stub
//        if (_consumer != null) 	_consumer.close();
//		
//	}
//
//	private KafkaConsumer<String, String> _consumer = null;
//	/**
//	 * consumer Connect
//	 * @return ConsumerConnector
//	 */
//	protected KafkaConsumer<String, String> consumer(){	
//		if (_consumer == null) 
//			_consumer =  new KafkaConsumer<String, String>(this.getProperties());
//		return _consumer;
//	}
//
//	/**
//	 * set Properties
//	 * @return Properties
//	 */
//	private Properties setProperties(){
//		return setProperties(getOffsetReset());
//	}
//	
//	/**
//	 * set Properties for OffsetReset
//	 * @return Properties
//	 */
//	private Properties setProperties(OffsetResetStrategy offsetReset){		
//		
//        Properties props = new Properties();
//        
//	    props.put("bootstrap.servers", this.getBootstrapList());
//	    props.put("group.id", this.getGroupID());
//	    props.put("enable.auto.commit", "false");
//	    props.put("auto.commit.interval.ms", "1000");
//	    props.put("session.timeout.ms", "30000");
//	    props.put("auto.offset.reset", offsetReset.name().toLowerCase());	    
//	    props.put("key.deserializer", StringDeserializer.class.getName());
//	    props.put("value.deserializer", StringDeserializer.class.getName());        
//        
//        return props; 
//	}
//
//}
