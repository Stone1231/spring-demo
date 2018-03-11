package com.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MqttResponse  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2418246197428080577L;

	@JsonProperty("status")
	private Integer status = 1;
	
	public MqttResponse() {
		super();
	}
	
	public MqttResponse(int status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
}
