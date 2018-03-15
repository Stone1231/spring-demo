package com.demo.service.kafka.base.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

	/*
	 * Kafka
	 */
	@Autowired
	private void setupConfig(
			@Value("${kafka.broker.list}") String bootstrapList,
			@Value("${kafka.zookeeper}") String zookeeper, 
			@Value("${kafka.groupId}") String groupId,
			@Value("${kafka.enable}") boolean enable) {
		
		Config.ZOOKEEPER = zookeeper;
		Config.BOOTSTRAP_LIST = bootstrapList;
		Config.GROUP_ID = groupId;
		Config.ENABLE = enable;
	}

	public static class Config {
		public static String ZOOKEEPER;
		public static String BOOTSTRAP_LIST;
		public static String GROUP_ID;
		public static boolean ENABLE;
	}

	/*
	 * Kafka
	 */
	@Autowired
	private void setupQueueName(
			@Value("${queue.kafka.topic.message}") String message,
			@Value("${queue.kafka.topic.other}") String other) {
		
		QueueName.MESSAGE_QUEUE = message;
		QueueName.OTHER_QUEUE = other;
	}

	public static class QueueName {

		public static String MESSAGE_QUEUE = "MESSAGE_QUEUE_QUEUE_TEST";
		public static String OTHER_QUEUE = "OTHER_QUEUE_TEST";
	}
}