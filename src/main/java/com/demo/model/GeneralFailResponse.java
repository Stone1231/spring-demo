package com.demo.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeneralFailResponse implements Serializable {

	private static final long serialVersionUID = 4413016851410831936L;
	
	private String error = "errorCode";	
	
	public GeneralFailResponse() {
		super();
	}
	
	public GeneralFailResponse(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
