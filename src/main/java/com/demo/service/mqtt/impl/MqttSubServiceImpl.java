package com.demo.service.mqtt.impl;


import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.demo.utils.JsonUtil;
import com.demo.model.Message;
import com.demo.service.mqtt.MqttPubService;
import com.demo.service.mqtt.MqttSubService;
import com.demo.queue.AbstractListenQueue;
import com.demo.queue.ListenQueue;
import com.demo.queue.ListenQueueGroup;
import com.demo.queue.ListenQueueGroupImpl;


@Service
public class MqttSubServiceImpl implements MqttSubService {

	@Autowired
	private MqttPubService pubService;
	
	@Value("${mqtt.threads}")
	private int threadCount;
	
	@Value("${mqtt.enable}")
	private boolean enable;

	private transient ListenQueueGroup<String> listenQueueGroup;

	private static final Logger LOGGER = LoggerFactory.getLogger(MqttSubServiceImpl.class);
	
	@PostConstruct
	protected void init() {
		if (enable) {
			buildQueue(threadCount);
			initChat();
		}
	}
	
	private void initChat() {
		try {
			MqttClient client = pubService.createClient();

			client.subscribe("chat/#", 0);
			
			client.setCallback(new MqttCallback() {

				@Override
				public void connectionLost(Throwable cause) { // Called when
																// the
																// client
																// lost the
																// connection
																// to the
																// broker
				}

				@Override
				public void messageArrived(String topic, MqttMessage message)
						throws Exception {
					listenQueueGroup.offer(new String(message.getPayload()));
				}
				
				//protected Integer j =0;

				@Override
				public void deliveryComplete(IMqttDeliveryToken token) {// Called
																		// when
																		// a
																		// outgoing
																		// publish
																		// is
																		// complete
				}
			});

			// client.connect();
			// client.subscribe("topic1/test", 1);

		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Persistence
	}

	protected Integer i =0;
	protected void setChat(String message) {

		Message msg = JsonUtil.readJSON(message, Message.class);
		
		switch (msg.getType()) {
		case "chat":
			try {
				//mongoMqttConversationDao.save(msg);
				LOGGER.info(message);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		default://group

			break;
		}
		
		
	}
	
	protected class Queue<E> extends AbstractListenQueue<String> {

		public void process(String message) {
			setChat(message);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void buildQueue(int threads) {
		ListenQueue<String> listenQueues[] = new Queue[threads];// thread����
		for (int i = 0; i < listenQueues.length; ++i) {
			Queue<String> queue = new Queue<String>();
			listenQueues[i] = queue;
		}
		this.listenQueueGroup = new ListenQueueGroupImpl(listenQueues);
	}

}
