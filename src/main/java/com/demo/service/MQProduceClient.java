package com.demo.service;

public interface MQProduceClient {

    public void sendMessage(String topicName, String message);
}
