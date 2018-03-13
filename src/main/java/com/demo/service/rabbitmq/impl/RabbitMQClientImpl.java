package com.demo.service.rabbitmq.impl;

import java.io.Closeable;
import java.io.IOException;

import com.demo.service.rabbitmq.RabbitMQClient;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class RabbitMQClientImpl implements RabbitMQClient, Closeable {

    private final Connection connection;

    public RabbitMQClientImpl(Connection connection) throws Exception {
        super();
        this.connection = connection;
    }

    @Override
    public Channel createChannel() throws IOException {
        return connection.createChannel();
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }

}
