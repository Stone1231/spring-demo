package com.demo.service.impl;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import com.demo.service.MQProduceClient;
import com.demo.service.RabbitMQClient;
import com.demo.service.RabbitService;
import com.demo.utils.StringUtil;
import com.demo.queue.AbstractListenQueue;
import com.demo.queue.ListenQueue;
import com.demo.queue.ListenQueueGroup;
import com.demo.queue.ListenQueueGroupImpl;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;


public class RabbitServiceImpl implements RabbitService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RabbitServiceImpl.class);
    
    /**
     *  最小發送佇列個數
     */
    private static final int MIN_SEND_QUEUE_SIZE = 1;

    /**
     *  最大發送佇列個數
     */
    private static final int MAX_SEND_QUEUE_SIZE = 10;

    /**
     *  發送訊息佇列監聽毫秒
     */
    @Value("${rabbitService.send.queue.listenMills}")
    private long sendMessageQueueListenMills = 1000L;

    /**
     *  發送訊息佇列是否開啟
     */
    @Value("${rabbitService.send.queue.enabled}")
    private boolean sendMessageQueueEnabled;

    /**
     *  發送訊息佇列個數
     */
    @Value("${rabbitService.send.queue.size}")
    private int sendMessageQueueSize;

    /**
     *  發送訊息服務佇列
     */
    private transient ListenQueueGroup<SendMessageArgs> SendMessageQueueGroup;
    
    // ------------------------------------------------------------------
    
    @Value("${rabbit.queueName}")
    private String queueName;
    @Value("${rabbit.exchangeName}")
    private String exchangeName;
    @Value("${rabbit.routingKey}")
    private String routingKey;
    @Value("${rabbit.enable}")
    private boolean rabbitEnable;
    
    @Autowired
    private MQProduceClient mqProduceClient;
    @Autowired
    private RabbitMQClient rabbitMQClient;
    
    private Channel channel;
    
    
    @PostConstruct
    protected void init() throws RuntimeException {
        // 建立發送用佇列
    	if (rabbitEnable) {
    		buildSendQueueGroup();
            this.recv(queueName);
    	}
    }
    
    /**
     *  建立發送用佇列
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected void buildSendQueueGroup() {
        if (sendMessageQueueEnabled) {
            //
            isBetween(
                sendMessageQueueSize, 
                MIN_SEND_QUEUE_SIZE, 
                MAX_SEND_QUEUE_SIZE,
                "The sendQueueSize is " + sendMessageQueueSize + " must be between " + MIN_SEND_QUEUE_SIZE + " and " + MAX_SEND_QUEUE_SIZE
            );
            //
            ListenQueue<SendMessageArgs> queues[] = new SendMessageQueue[this.sendMessageQueueSize];
            for (int i = 0; i < queues.length; ++i) {
                SendMessageQueue<SendMessageArgs> queue = new SendMessageQueue<SendMessageArgs>();
                queue.setListenMills(this.sendMessageQueueListenMills);
                queues[i] = queue;
            }
            this.SendMessageQueueGroup = new ListenQueueGroupImpl(queues);
            //
            LOGGER.info("SendQueueGroup[" + this.sendMessageQueueSize + "] listen " + sendMessageQueueListenMills + " mills.");
        }
    }
    
    private void isBetween(int value, int min, int max, String message) {
        if (!(value >= min && value <= max))
            throw new IllegalArgumentException(message);
    }
    
    /**
     *  SendMessageArgs() 參數
     */
    protected static class SendMessageArgs implements Serializable {
        
        private static final long serialVersionUID = 6927622457511691754L;

        private String queueName;

        private String message;

        public SendMessageArgs(String queueName, String message) {
            this.queueName = queueName;
            this.message = message;
        }

        public String getQueueName() {
            return queueName;
        }

        public void setQueueName(String queueName) {
            this.queueName = queueName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String toString() {
            ToStringBuilder builder = new ToStringBuilder(this);
            builder.appendSuper(super.toString());
            builder.append("queueName", queueName);
            builder.append("message", message);
            return builder.toString();
        }
    }
    
    /**
     *  Queue Offer
     */
    private boolean sendMessageByQueue(String queueName, String message) {
        SendMessageArgs args = new SendMessageArgs(queueName, message);
        return SendMessageQueueGroup.offer(args);
    }
    
    /**
     *  發送訊息用佇列
     */
    protected class SendMessageQueue<E> extends AbstractListenQueue<SendMessageArgs> {

        public SendMessageQueue() {
        	//建一個專屬於這個佇列的mqProduceClient
        }

        public void process(SendMessageArgs args) {
            sendMessage(args.getQueueName(), args.getMessage());
        }
    }
    
    private boolean sendMessage(String queueName, String message) {
        boolean result = false;
        if (message == null) {
        	return result;
        }
        try {
        	mqProduceClient.sendMessage(queueName, message);
        	result = true;
        } catch (Exception ex) {
        	LOGGER.error("sendMessage failed! queueName: {}, message: {}", queueName, message, ex);
        }
        return result;
    }
    
    @Override
    public boolean send(String queueName, String message) {
        if (sendMessageQueueEnabled) {
            // 使用佇列更新
            sendMessageByQueue(queueName, message);
        } else {
            sendMessage(queueName, message);
        }
        return false;
    }
    
    @Override
    public void recv(String queueName) {
    	if (StringUtil.isNullOrEmpty(queueName)) {
    		return;
    	}
        try {
            /*
             *  可以解決產生無限線程的問題，但是當瞬間並發量非常大的時候，
             *  會造成大量對象堆積到隊列中無法及時消費，將可能造成大量訊息
             *  出現timeout，但是有設定basicQos可避免大量訊息併發
             *  (basicQos 控制從service拿取限定數量訊息，目前設定為:1)
             */
            channel = rabbitMQClient.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
            channel.exchangeDeclare(exchangeName, "topic", true);
            channel.queueBind(queueName, exchangeName, routingKey);
            channel.basicQos(1);
            channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    channel.basicAck(envelope.getDeliveryTag(), false);
                    try {
                        String message = new String(body, "UTF-8");
                        if (StringUtil.isNullOrEmpty(message)) {
                        	LOGGER.info(" [x] Received '" + message + "'");
                            return;
                        }
                        handleMessage(message);
                        Thread.sleep(1000);
                    } catch (Exception ex) {
                    	LOGGER.error("rabbitMQworker exception", ex);
                    }
                }
            });
        } catch (IOException e) {
        	LOGGER.error("IOException", e);
        }
    }
    
    private void handleMessage(String message) {
    	LOGGER.info("received message {}", message);
//    	if (StringUtils.isNullOrEmpty(message)) {
//            return;
//        }
//    	SSOMessageBody messageBody = StringUtils.readJSON(message, SSOMessageBody.class);
//    	if (messageBody == null || StringUtils.isNullOrEmpty(messageBody.getSsoId())) {
//    		logger.error("received message is null");
//    		return;
//    	}
//    	Trail trail = new Trail();
//		trail.setDbId(messageBody.getDbId());
//		trail.setTableId(messageBody.getTableId());
//		long seq = globalSeqDao.nextFriendSeq();
//		String ssoId = messageBody.getSsoId();
//		if (messageBody.getAction().equals("/user/create")) {
//			Account account = new Account();
//			account.setSeq(seq);
//			account.setSsoId(ssoId);
//			account.setDisplayName(messageBody.getDisplayName());
//			account.setPhotoUrl(messageBody.getPhotoUrl());
//			account.setCreateTime(messageBody.getTime());
//			account.setModifyTime(messageBody.getTime());
//			accountDao.insertAccount(trail, account);
//		} else if (messageBody.getAction().equals("/user/update")) {
//			Account account = accountDao.getAccount(trail);
//			account.setDisplayName(messageBody.getDisplayName());
//			account.setPhotoUrl(messageBody.getPhotoUrl());
//			account.setCreateTime(messageBody.getTime());
//			account.setModifyTime(messageBody.getTime());
//			accountDao.updateAccount(trail, account);
//		} else {
//			accountDao.deleteAccount(trail);
//		}
    }
}
