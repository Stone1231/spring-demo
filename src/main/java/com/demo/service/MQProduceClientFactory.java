package com.demo.service;

public interface MQProduceClientFactory<T> extends ClientFactory<T> {

    public MQProduceClient createProduceClient();
}
