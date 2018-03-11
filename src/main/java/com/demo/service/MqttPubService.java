package com.demo.service;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.demo.model.MqttRequest;
import com.demo.model.MqttResponse;


public interface MqttPubService {

	MqttResponse sendMessageSync(MqttRequest request);

	MqttResponse sendMessage(MqttRequest request);

	MqttClient createClient();
}
