package com.demo.service.kafka.base;

import com.demo.service.kafka.base.impl.KafkaConsumerImpl.Version;

public interface KafkaConsumer extends KafkaConsumerStream, KafkaConsumerRecord{
	/**
	 * Use version
	 * @return Version
	 */
	public Version getVersion();

}
