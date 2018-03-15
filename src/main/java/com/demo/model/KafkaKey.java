package com.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KafkaKey {	
	
	public KafkaKey(String key){
		this.key = key;
	}
	
	public KafkaKey(String key, Callback callback){
		this.key = key;
		this.callback = callback;
	}

	public KafkaKey() {
		// TODO Auto-generated constructor stub
	}

	@JsonProperty("key")
	String key;	
	public String getKey() {
		return key;
	}
	public void setMessage(String key) {
		this.key = key;
	}	
	
	@JsonProperty("callback")
	Callback callback;
	public Callback getCallback() {
		return callback;
	}

	public void setCallBack(Callback callback) {
		this.callback = callback;
	}
}