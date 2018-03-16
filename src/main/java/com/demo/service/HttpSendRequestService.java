package com.demo.service;

import org.springframework.http.HttpHeaders;

import com.demo.model.Feeback;
import com.demo.model.WebServiceQueue;

public interface HttpSendRequestService {

	/**
	 * Real Time call Service
	 * 
	 * @param queue: WebServiceQueue
	 * @return
	 * @throws Exception
	 */
	public Feeback callService(WebServiceQueue queue);
	
	/**
	 * Real Time call Service
	 * 
	 * @param url: API URI
	 * @param headers: request http headers
	 * @return
	 * @throws Exception
	 */
	public Feeback callService(String url, HttpHeaders headers);
	
	/**
	 * Real Time call Service
	 * 
	 * @param url: API URI
	 * @param headers: request http headers
	 * @param request: API request body
	 * @return
	 * @throws Exception
	 */
	public <T> Feeback callService(String url, HttpHeaders headers, T request);

}