package com.demo.service;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.base.BaseTest;
import com.demo.service.rabbitmq.RabbitService;
import com.demo.utils.StringUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

public class RabbitMQTest extends BaseTest {

//	 @Rule
//	 public BenchmarkRule benchmarkRule = new BenchmarkRule();

	@Autowired
	private RabbitService service;

	@Test
	public void sendMsg() {

		service.recv("queueName");

		service.send("queueName", "hi rabbit!!!");

		while (true) {

		}
	}

	@Test
	public void sendMsgs() {

		service.recv("queueName");

		int i = 0;
		while (i < 20000) {
			service.send("queueName", "message");
			i++;
		}

		while (true) {

		}
	}

	@Value("${rabbit.host}")
	private String host;
	@Value("${rabbit.port}")
	private Integer port;
	@Value("${rabbit.username}")
	private String username;
	@Value("${rabbit.password}")
	private String password;
	@Value("${rabbit.queueName}")
	private String queueName;

	@Value("${rabbit.exchangeName}")
	private String exchangeName;
	@Value("${rabbit.routingKey}")
	private String routingKey;
	@Value("${rabbit.enable}")
	private boolean rabbitEnable;
	
	public void sendSource() {
		
		Channel channel = getChannel();
		
		String message = "hi rabbit!!";
		
		try {
			channel.queueDeclare(queueName, true, false, false, null);
			channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void recvSource() {
		
		sendSource();
		
		Channel channel = getChannel();

		try {
			channel.queueDeclare(queueName, true, false, false, null);
			channel.exchangeDeclare(exchangeName, "topic", true);
			channel.queueBind(queueName, exchangeName, routingKey);
			channel.basicQos(1);
			channel.basicConsume(queueName, false, new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				channel.basicAck(envelope.getDeliveryTag(), false);
				try {
					String message = new String(body, "UTF-8");
					if (StringUtil.isNullOrEmpty(message)) {
						return;
					}
					System.out.println(" Received '" + message + "'");
					//handleMessage(message);
					Thread.sleep(1000);
				} catch (Exception ex) {
					System.out.println("rabbitMQworker exception" + ex);
				}
			}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (true) {

		}
	}

	private Channel getChannel() {
		Connection connection;
		Channel channel = null;

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);

		ExecutorService es = Executors.newFixedThreadPool(10);
		try {
			connection = factory.newConnection(es);
			channel = connection.createChannel();

			// channel.queueDeclare(queueName, true, false, false, null);
			// channel.basicPublish("", queueName,
			// MessageProperties.PERSISTENT_TEXT_PLAIN,
			// message.getBytes("UTF-8"));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return channel;
	}

}
