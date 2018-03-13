package com.demo.service.rabbitmq.impl;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.demo.service.rabbitmq.MQProduceClient;
import com.demo.service.rabbitmq.MQProduceClientFactory;
import com.demo.service.rabbitmq.RabbitMQClient;
import com.demo.utils.StringUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;


public class RabbitMQClientFactoryImpl implements MQProduceClientFactory<RabbitMQClient> {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQClientFactoryImpl.class);
        
    @Value("${rabbit.host}")
    private String host;   
    @Value("${rabbit.port}")
    private Integer port;  
    @Value("${rabbit.username}")
    private String username;   
    @Value("${rabbit.password}")
    private String password;
    private Connection connection;    
    private Channel channel;   
    private ConnectionFactory factory;
    private RabbitMQClient rabbitMQClient;
    
//    public RabbitServiceImpl() {
//    }
    
    @PostConstruct
    protected void init() throws RuntimeException {
        
        // 設定連線
        factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        
        // 自動重新啟動
        factory.setAutomaticRecoveryEnabled(true);
        // 在恢復期間將固定間格10秒進行重試(默認值5秒)
        factory.setNetworkRecoveryInterval(10000);
        /*
         *  心跳超時設置為5秒(3.6.x開始的版本中其默認值為:60)
         *  5到20秒範圍內的值對於大多數環境是最佳的
         */
        factory.setRequestedHeartbeat(5);
    }
    
    @Override
    public RabbitMQClient createClient() {
        if (rabbitMQClient != null || StringUtil.isNullOrEmpty(username) || StringUtil.isNullOrEmpty(password)) {
            return rabbitMQClient;
        }
        try {
        	ExecutorService es = Executors.newFixedThreadPool(10);
            connection = factory.newConnection(es);
            rabbitMQClient = new RabbitMQClientImpl(connection);
        } catch (Exception ex) {
        	LOGGER.error("createClient failed!", ex);
        }
        return rabbitMQClient;
    }
    
    @Override
    public MQProduceClient createProduceClient() {
        try {
            channel = rabbitMQClient.createChannel();
        } catch (IOException ioe) {
        	LOGGER.error("create Produce Client failed!", ioe);
        }
        return new MQProduceClient() {
            @Override
            public void sendMessage(String queueName, String message) {
                try {
                	channel.queueDeclare(queueName, true, false, false, null);
                    channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
                } catch (IOException ioe) {
                	LOGGER.error("sendMessage failed!", ioe);
                }
            }
        };
    }
}
