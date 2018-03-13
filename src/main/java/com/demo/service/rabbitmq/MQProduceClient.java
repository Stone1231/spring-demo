package com.demo.service.rabbitmq;

public interface MQProduceClient {

    public void sendMessage(String topicName, String message);
}
