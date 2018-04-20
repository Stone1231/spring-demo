package com.demo.service;

import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;
import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.base.BaseTest;
import com.demo.model.Feeback;
import com.demo.model.Message;
import com.demo.service.kafka.MessageKafkaReceiveService;
import com.demo.service.kafka.MessageKafkaSendService;
import com.demo.service.kafka2.MessageKfService;
import com.demo.service.kafka2.ReceiveListener;
import com.demo.service.kafka.base.KafkaBaseReceiveListener;
import com.demo.service.kafka.base.impl.KafkaConfig;
import com.demo.utils.SerializeUtil;
import com.demo.utils.StringUtil;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.TopicFilter;
import kafka.consumer.Whitelist;
import kafka.message.MessageAndMetadata;


public class KafkaTest extends BaseTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Resource
	private MessageKafkaSendService sendService;

	@Resource
	MessageKafkaReceiveService receiveService;
	
	@Resource
	MessageKfService messageKfService;

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void sendOld() throws Exception {

		String id = StringUtil.randomString(10);

		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("kafka");
		message.setBody("old kafka!");
		message.setReceiver("receiver");
		message.setSender("sender");
		
		System.out.print(String.format("[send key]:%s \n ", id));
		sendService.sendMessage(id, message);
	}	
	
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void receiverOld() throws Exception {
		
		sendOld();
		
		receiveService.messageReceiver(new KafkaBaseReceiveListener<Message>() {

			@Override
			public Feeback receiveMessage(String key, Message message) {
				// TODO Auto-generated method stub
				System.out.print(String.format("My receiveMessage (key, message)\n key:%s \n message:%s\n", key,
						StringUtil.writeJSON(message)));
				return new Feeback(Feeback.Status.success, "測試");
			}
		});

		Thread.sleep(1000000);
		receiveService.shutdown();
	}	
	
	 @Test
	 @BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	//@Before
	public void sendNew() throws Exception {

		String id = StringUtil.randomString(10);

		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("kafka");
		message.setBody("new kafka!");
		message.setReceiver("receiver");
		message.setSender("sender");
		
		String topic = KafkaConfig.QueueName.MESSAGE_QUEUE;
		messageKfService.send(topic, id, message);
		System.out.print(String.format("[new send key]:%s \n ", id));
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void receiverNew() throws Exception {
		
		sendNew();
		
		String topic = KafkaConfig.QueueName.MESSAGE_QUEUE;
		messageKfService.receiver(
				topic,
				new ReceiveListener<Message>() {

					@Override
					public Class<Message> getClazz() {
						// TODO Auto-generated method stub
						return Message.class;
					}

					@Override
					public Feeback receiveMessage(String key, Message message) throws Exception {
//						// TODO Auto-generated method stub
						System.out.print(String.format("My new receiveMessage (key, message)\n key:%s \n message:%s\n", key,
								StringUtil.writeJSON(message)));
						return new Feeback(Feeback.Status.success, "測試");
					}
				});

		Thread.sleep(1000000);
		receiveService.shutdown();
	}

	@Value("${kafka.zookeeper}")
	private String connect;

	@Value("${kafka.groupId}")
	private String groupId;

	@Value("${kafka.broker.list}")
	private String brokers;

	@Value("${kafka.broker.list}")
	private String servers;

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	//@Before
	public void sendSource() {

		Properties props = new Properties();
		props.put("metadata.broker.list", brokers);
		props.put("bootstrap.servers", servers);
		// props.put("compression.codec", "1");
		props.put("request.required.acks", "1");
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());//ByteArraySerializer.class.getName()

		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
		String topic = KafkaConfig.QueueName.MESSAGE_QUEUE;

		String id = StringUtil.randomString(10);
		Message message = new Message();
		message.setMsgId(id);
		message.setLogDate(1L);
		message.setType("kafka");
		message.setBody("source kafka!");
		message.setReceiver("receiver");
		message.setSender("sender");
		
		String key = id;
		String value = StringUtil.writeJSON(message);
		
		Integer partition = 0;

		// 建立 資料 物件
		ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, partition, key, value);
		System.out.print(String.format("[send key]:%s \n ", key));

		// 執行 傳送後關閉 端口
		producer.send(data);
		partition++;

		producer.close();
	}

	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void receiverSource() {
		
		sendSource();

		Properties props = new Properties();

		props.put("zookeeper.connect", connect);
		props.put("group.id", groupId);

		// 設定 與 zookeeper的連線的 timeout 時間
		props.put("zookeeper.session.timeout.ms", "30000");
		// 設定 ZooKeeper集群中l eader和follower的同步時間
		props.put("zookeeper.sync.time.ms", "1000");

		props.put("auto.commit.enable", "false");

		// 自?提交 offset 的時間間隔
		props.put("auto.commit.interval.ms", "1000");
		props.put("auto.offset.reset", "smallest");
		props.put("rebalance.max.retries", "5");
		props.put("rebalance.backoff.ms", "30000");
		props.put("request.required.acks", "-1");

		ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
		
		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		String topic = KafkaConfig.QueueName.MESSAGE_QUEUE;
		topicCountMap.put(topic, 1);//多個就累加 2, 3, 4...
		
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
		
		List<KafkaStream<byte[], byte[]>> topicMessages = consumerMap.get(topic);
		consumer.commitOffsets(true);
		while (true) {

			for (KafkaStream<byte[], byte[]> topicMessage : topicMessages) {
				ConsumerIterator<byte[], byte[]> consumerIterator = topicMessage.iterator();
				while (consumerIterator.hasNext()) {
					MessageAndMetadata<byte[], byte[]> record = consumerIterator.next();
					// String topic = record.topic();
					// String key = new String(record.key());
					// //transNull2EmptyStr(record.key());
					// String message = new String(record.message());
					// //transNull2EmptyStr(record.message());

					String key = new String(record.key());
					String message = new String(record.message());
					System.out.print(key);
					System.out.print(String.format("[receiver key]:%s \n ", key));
					System.out.print(String.format("[receiver message]:%s \n ", message));
					
					// String key = SerializeUtil.dekryo(record.key(),
					// String.class);

//					Message message = SerializeUtil.deserialize(record.message());
//					System.out.print(String.format("My receiveMessage (key,message)\n key:%s \n message:%s\n", key, StringUtil.writeJSON(message)));
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}