package com.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import com.demo.model.Feeback;
import com.demo.model.Feeback.Status;
import com.demo.model.WebServiceQueue;
import com.demo.service.RestService;
import com.demo.service.ShareHttpSendRequestService;
import com.demo.utils.StringUtil;

@Service
public class ShareHttpSendRequestServiceImpl implements ShareHttpSendRequestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShareHttpSendRequestServiceImpl.class);
	
	@Autowired
	private RestService restService;
	

	public ShareHttpSendRequestServiceImpl(){
		
	}	
	
//	@PostConstruct
//	public void init() {
//		
//		//收訊息
//		_service.messageReceiver(this);		
//	}	
	
	
	@Override
	/**
	 * Real Time call Service
	 * 
	 * @param queue: WebServiceQueue
	 * @return
	 * @throws Exception
	 */
	public Feeback callService(WebServiceQueue queue){		
		
		queue.setTryCount(queue.getTryCount() + 1);
		queue.setTryDate(System.currentTimeMillis());		

		HttpHeaders headers = getHttpHeaders(queue.getHeaders());
		Feeback feeback = callService(queue.getApi(), headers, queue.getRequest());		
		queue.setResponse(feeback);
		return feeback;
	}

	@Override
	/**
	 * Real Time call Service
	 * 
	 * @param url: API URI
	 * @param headers: request http headers
	 * @return
	 * @throws Exception
	 */
	public Feeback callService(String url, HttpHeaders headers){
		return callService(url, headers, null);
	}
	
	@Override
	/**
	 * Real Time call Service
	 * 
	 * @param url: API URI
	 * @param headers: request http headers
	 * @param request: API request body
	 * @return
	 * @throws Exception
	 */
	public <T> Feeback callService(String url, HttpHeaders headers, T request){
		
		try {
			ResponseEntity<String> response = restService.sendRequest(url, HttpMethod.POST, headers, request);
			if (response.getStatusCode() == HttpStatus.OK)
				return new Feeback(Status.success, response.getBody());
			else {
				return new Feeback(Status.failure, response.getBody());
			}
		} catch (HttpClientErrorException e) {
			return new Feeback(Status.failure, e);
		} catch (HttpServerErrorException e) {
			System.out.println(e.getResponseBodyAsString());
			return new Feeback(Status.failure, e);
		} catch (Exception e) {
			return new Feeback(Status.failure, e);
		}
	}
	
	/**
	 * Convert Object JSON to HttpHeaders
	 * 
	 * @param headers
	 * @return
	 */
	private HttpHeaders getHttpHeaders(Object headers){
		
		HttpHeaders httpHeaders = null;
		String headerJson = StringUtil.writeJSON(headers);
		if (headerJson != null) {
			httpHeaders = StringUtil.readJSON(headerJson, HttpHeaders.class);
		}
		
		return httpHeaders;
	}
}