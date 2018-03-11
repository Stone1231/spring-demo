package com.demo.model;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.demo.utils.StringUtil;

public class Feeback extends Object{
	
	private Status status;
	private int errorCode;
	private String errorMessage;
	private String message;
	private HttpServerErrorException httpException;
	private HttpClientErrorException httpClientException;

	public Feeback(){
		
	}
	
	public Feeback(String message){
		this(Status.success, message);
	}

	public Feeback(Status status, String message){
		this.status = status;
		
		if (status == Status.success)
			this.message = message;
		else {
			this.errorMessage = message;
		}
	}
	
	public Feeback(Status status, String message, String errorMessage){
		this.status = status;
		this.message = message;
		this.errorMessage = errorMessage;
	}
	
	public Feeback(Exception exception){
		this(Status.failure, exception);
	}
	
	public Feeback(Status status, Exception exception){		
		this.status = status;
		this.errorMessage = exception.getMessage();
		this.message = exception.getMessage();
	}
	
	public Feeback(Status status, HttpClientErrorException httpClientException){		
		this.status = status;
		this.httpClientException = httpClientException;
	}
	
	public Feeback(Status status, HttpServerErrorException httpException){		
		this.status = status;
		this.httpException = httpException;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	public HttpServerErrorException getHttpException() {
		return httpException;
	}

	public void setHttpException(HttpServerErrorException httpException) {
		this.httpException = httpException;
	}
	
	public HttpClientErrorException getHttpClientException() {
		return httpClientException;
	}

	public void setHttpClientException(HttpClientErrorException httpClientException) {
		this.httpClientException = httpClientException;
	}

	@Override
	public String toString() {
		return StringUtil.writeJSON(this);
	}

	@JsonFormat(shape= JsonFormat.Shape.STRING)
	public enum Status{
		success(1),
		reTry(0),
		failure(-1),
		;
		
		private final int code;
		
		private Status(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}
	}	
}