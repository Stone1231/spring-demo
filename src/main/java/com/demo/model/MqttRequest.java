package com.demo.model;

public class MqttRequest {
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getQos() {
		return qos;
	}
	public void setQos(int qos) {
		this.qos = qos;
	}
	public Boolean getRetain() {
		return retain;
	}	
	public void setRetain(Boolean retain) {
		this.retain = retain;
	}
	private String topic;
	private String message;
	private int qos;
	private Boolean retain;
}
