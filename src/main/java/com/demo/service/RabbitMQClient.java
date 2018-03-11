package com.demo.service;

import java.io.IOException;

import com.rabbitmq.client.Channel;

public interface RabbitMQClient {

    public Channel createChannel() throws IOException;
}
