package com.demo.service.kafka.base;

public interface KafkaChatService<T> extends KafkaChatSendMessageServices<T>, KafkaChatReceiveMessageServices<T>{

}
