package com.demo.service.rabbitmq;

public interface RabbitService
{
    // 發送
    boolean send(String queueName, String message);

    // 接收
    void recv(String queueName);
}
