package com.demo.service.rabbitmq;

public interface MQProduceClientFactory<T> extends ClientFactory<T> {

    public MQProduceClient createProduceClient();
}
