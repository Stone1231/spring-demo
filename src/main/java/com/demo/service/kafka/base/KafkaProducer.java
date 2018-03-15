package com.demo.service.kafka.base;

import com.demo.service.kafka.base.impl.KafkaProducerImpl.Version;

public interface KafkaProducer extends KafkaProducerKeyedMessage, KafkaProducerRecord {

	/**
	 * Use version
	 * @return Version
	 */
	public Version getVersion();
}
