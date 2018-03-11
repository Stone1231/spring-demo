package com.demo.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

public interface RestService {
	public <T> T sendPostRequestForObj(String url, HttpHeaders headers, Object request, Class<T> responseType);

	public <T> ResponseEntity<String> sendRequest(String url, HttpMethod method, HttpHeaders headers, T requestBody)
			throws HttpClientErrorException, HttpServerErrorException, Exception;
	
}