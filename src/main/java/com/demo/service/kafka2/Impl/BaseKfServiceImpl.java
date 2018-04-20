package com.demo.service.kafka2.Impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.demo.service.kafka2.ReceiveListener;
import com.demo.service.kafka2.BaseKfService;
import com.demo.utils.StringUtil;
import kafka.common.MessageStreamsExistException;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.consumer.TopicFilter;
import kafka.consumer.Whitelist;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import com.demo.model.Feeback;
import com.demo.service.impl.ThreadService;


@Service
public class BaseKfServiceImpl<T> implements BaseKfService<T> {

	@Value("${kafka.zookeeper}")
	private String connect;

	@Value("${kafka.groupId}")
	private String groupId;

	@Value("${kafka.broker.list}")
	private String brokers;

	@Value("${kafka.broker.list}")
	private String servers;

	@Value("${kafka.enable}")
	private boolean enable;

	protected KafkaProducer<String, String> producer = null;

	protected ConsumerConnector consumer = null;

	private ThreadService threadService = new ThreadService();

	private final int defaultZeroPenRestCount = 3;

	private static final Logger LOGGER = LoggerFactory.getLogger(BaseKfServiceImpl.class);

	protected void init() {
		initProducer();
		initConsumer();
	}

	private void initProducer() {
		Properties props = new Properties();
		props.put("metadata.broker.list", brokers);
		props.put("bootstrap.servers", servers);
		// props.put("compression.codec", "1");
		props.put("request.required.acks", "1");
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		producer = new KafkaProducer<String, String>(props);
	}

	private void initConsumer() {
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

		consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
	}

	@Override
	public void send(String topic, String key, T msg) {
		send(Arrays.asList(topic), key, msg);
	}

	@Override
	public void send(List<String> topics, String key, T msg) {
		Integer partition = 0;
		for (String topic : topics) {

			ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, partition, key,
					StringUtil.writeJSON(msg));

			producer.send(data);

			partition++;
		}

	}

	@Override
	public void receiver(String topic, ReceiveListener<T> listener) {
		receiver(Arrays.asList(topic), listener);
	}

	@Override
	public void receiver(List<String> topics, final ReceiveListener<T> listener) {
		threadService.doStart();
		threadService.sumbit(new Runnable() {
			@Override
			public void run() {
				while (enable) {
					try {
						execute(topics, listener);
					} catch (Exception ace) {
						sleep(5000);
					}
				}
			}
		});
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (Exception e) {
			// LOGGER.error("sleep", e);
		}
	}

	private void execute(List<String> topics, ReceiveListener<T> listener) {

		int restCount = 0;
		while (enable) {
			try {
				int count = receiveTopics(topics, listener);

				if (count == 0)
					restCount++;
				if (restCount > defaultZeroPenRestCount) {
					restCount = 0;
					sleep(10000);
				}
			} catch (Exception ace) {
				LOGGER.error("Connection Failed when retsuper.riving queue URL, sleep 5000ms", ace);
				sleep(5000);
			}
		}
	}

	private int receiveTopics(List<String> topics, ReceiveListener<T> listener) {
		// TODO Auto-generated method stub
		int count = 0;
		Map<String, List<KafkaStream<byte[], byte[]>>> dataSource = dataSourceStream(topics);
		LOGGER.info("kafka:dataSourceStream");
		for (String topic : topics) {
			List<KafkaStream<byte[], byte[]>> topicsMessages = dataSource.get(topic);
			for (KafkaStream<byte[], byte[]> topicMessage : topicsMessages) {
				count = count + receiveTopic(topicMessage, listener);
			}
		}

		return count;
	}

	private Map<String, List<KafkaStream<byte[], byte[]>>> dataSourceStream(List<String> topics) {

		Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap;

		int topicsCount = 1;
		for (String topic : topics) {
			LOGGER.info(String.format("kafka:topicsCount = %s", topicsCount));
			topicCountMap.put(topic, new Integer(topicsCount++));
			// 代表這個VM要開幾條通道
		}

		try {
			consumerMap = consumer.createMessageStreams(topicCountMap);
			LOGGER.info("kafka:createMessageStreams");
		} catch (MessageStreamsExistException me) {
			consumerMap = new HashMap<String, List<KafkaStream<byte[], byte[]>>>();
			LOGGER.info("kafka:createMessageStreams again");
			for (String topic : topics) {
				TopicFilter filter = new Whitelist(topic);
				consumerMap.put(topic, consumer.createMessageStreamsByFilter(filter));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("receiveTopicMessages", e);
			throw e;
		} finally {
			// closeConnect();
			// LOGGER.info("kafka:closeConnect");
			LOGGER.info("kafka:finally");
		}
		return consumerMap;
	}

	private int receiveTopic(KafkaStream<byte[], byte[]> records, ReceiveListener<T> listener) {

		int count = 0;
		// Map<TopicAndPartition, OffsetAndMetadata> offsets = new
		// HashMap<TopicAndPartition, OffsetAndMetadata>();
		ConsumerIterator<byte[], byte[]> consumerIterator = records.iterator();
		while (consumerIterator.hasNext()) {
			// for (MessageAndMetadata<byte[], byte[]> record : records){
			MessageAndMetadata<byte[], byte[]> record = consumerIterator.next();
			String topic = record.topic();
			String key = new String(record.key());// transNull2EmptyStr(record.key());
			String message = new String(record.message());// transNull2EmptyStr(record.message());
			Feeback metadata = null;

			T retMessage = null;
			try {
				consumer.commitOffsets(true);
				if (listener != null){
					try {
						retMessage = StringUtil.readJSON(message, listener.getClazz());
						metadata = listener.receiveMessage(key, retMessage);
					} catch (Exception e) {
						LOGGER.error("receiveMessage", e);
						metadata = new Feeback(Feeback.Status.reTry, e);
					} finally {
						// reTry for reSend to
						if (metadata != null && metadata.getStatus() == Feeback.Status.reTry) {
							if (key != null && retMessage != null) {
								// send(key, retMessage); 收跟發沒關係, 應該不用重發
							} else {
								LOGGER.error("receiveMessage kafkaKey retMessage is null");
							}
						}
					}
				} else {
					metadata = new Feeback("");
				}
			} catch (Exception e) {
				// 錯誤還原
				metadata = new Feeback(e);
				consumer.commitOffsets(false);
				LOGGER.error(String.format("[%s] rollback (key[%s]:%s)", topic, record.offset(), key, e));
			} finally {
				count++;
			}
		}

		return count;
	}
}
