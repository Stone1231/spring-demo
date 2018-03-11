package com.demo.service.impl;

import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.demo.model.MqttRequest;
import com.demo.model.MqttResponse;
import com.demo.service.MqttPubService;
import com.demo.queue.AbstractListenQueue;
import com.demo.queue.ListenQueue;
import com.demo.queue.ListenQueueGroup;
import com.demo.queue.ListenQueueGroupImpl;

@Service
public class MqttPubServiceImpl implements MqttPubService {

	@Value("${mqtt.host}")
	private String host;

	@Value("${mqtt.userName}")
	private String userName;

	@Value("${mqtt.password}")
	private String password;

	@Value("${mqtt.threads}")
	private int threadCount;

	@Value("${mqtt.cleanStart}")
	private boolean cleanStart;

	@Value("${mqtt.reconnectionAttemptMax}")
	private int reconnectionAttemptMax;

	@Value("${mqtt.reconnectionDelay}")
	private int reconnectionDelay;

	@Value("${mqtt.keepAlive}")
	private short keepAlive;

	@Value("${mqtt.sendBufferSize}")
	private int sendBufferSize;
	
	@Value("${mqtt.enable}")
	private boolean enable;

	private transient ListenQueueGroup<MqttRequest> listenQueueGroup;

	private static final Logger LOGGER = LoggerFactory.getLogger(MqttPubServiceImpl.class);
	
	@PostConstruct
	protected void init() {
		if (enable) {
			initOptions();
			buildQueue(threadCount);
		}
	}
	
	private MqttConnectOptions options;	
	private void initOptions(){
		options = new MqttConnectOptions();
		options.setUserName("pahoUser");
		options.setPassword("password".toCharArray());
		
		options.setCleanSession(cleanStart);
		// 设置重新连接的次数
		
		//options .setReconnectAttemptsMax(reconnectionAttemptMax);
		// 设置重连的间隔时间
		//options.setReconnectDelay(reconnectionDelay);
		// 设置心跳时间
		options.setKeepAliveInterval(keepAlive);
		// 设置缓冲的大小
		//options.setSendBufferSize(sendBufferSize * 1024 * 1024);
	}
	
	@Override
	public MqttClient createClient() {
		MqttClient client = null;
		try {
			client = new MqttClient(
					host,
					MqttClient.generateClientId(), // ClientId
					new MemoryPersistence());

			client.connect(options);
			
			//client.publish(topic, payload, qos, retained);

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Persistence
		
		return client;
	}
	
	MqttClient _client = null;
	@Override
	public MqttResponse sendMessage(MqttRequest request) {
		MqttResponse response = new MqttResponse();

		try {			
			if(_client==null){
				_client = createClient();
			}

			send(request, _client);
			
			response.setStatus(1);
		} catch (Exception e) {
			// TODO: handle exception
			response.setStatus(0);
		}

		return response;
	}

	@Override
	public MqttResponse sendMessageSync(MqttRequest request) {
		MqttResponse response = new MqttResponse();

		try {
			this.listenQueueGroup.offer(request);
			response.setStatus(1);
		} catch (Exception e) {
			// TODO: handle exception
			response.setStatus(0);
		}

		return response;
	}
	
	protected void send(MqttRequest request, MqttClient client) {
		try {

			if(!client.isConnected()){				
				client.connect();
			}

			client.publish(
					request.getTopic(), 
					request.getMessage().getBytes(), 
					request.getQos(), 
					request.getRetain());

		} catch (Exception ex) {
			// TODO: handle exception
			LOGGER.error("##mqtt##", ex);
		}
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void buildQueue(int threads) {
		ListenQueue<MqttRequest> listenQueues[] = new Queue[threads];
		for (int i = 0; i < listenQueues.length; ++i) {
			Queue<MqttRequest> queue = new Queue<MqttRequest>();
			listenQueues[i] = queue;
		}
		this.listenQueueGroup = new ListenQueueGroupImpl(listenQueues);
	}

	protected class Queue<E> extends AbstractListenQueue<MqttRequest> {

		public Queue(){		
			try {
				client = createClient();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void process(MqttRequest request) {	
			send(request, client);
			//send(request);
		}
		
		MqttClient client;
	}

}
