package com.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.demo.model.GeneralFailResponse;
import com.demo.service.RestService;
import com.demo.utils.StringUtil;

@Service
public class RestServiceImpl implements RestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestServiceImpl.class);

	@Value("${http.proxy.enabled}")
	private boolean proxyEnabled;
	@Value("${http.proxy.host}")
	private String proxyHost;
	@Value("${http.proxy.port}")
	private int proxyPort;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public <T> T sendPostRequestForObj(String url, HttpHeaders headers,
			Object request, Class<T> responseType) {
		try {
			HttpEntity<?> entity = new HttpEntity<Object>(request, headers);
			ResponseEntity<T> result = restTemplate.exchange(url,
					HttpMethod.POST, entity, responseType);
			if (result.getStatusCode() == HttpStatus.OK) {
				return result.getBody();
			}
		} catch (HttpClientErrorException e) {
			LOGGER.info("POST request error - {} : {}", url, e.getMessage());
		} catch (HttpServerErrorException e) {
			LOGGER.error("POST request error - {} : {}", url, e.getMessage());
		} catch (Exception e) {
			LOGGER.error("POST request error - {}", url, e);
		}
		return null;
	}

	@PostConstruct
	public void init() {
		PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(
				30, TimeUnit.SECONDS);
		pollingConnectionManager.setMaxTotal(1000);
		pollingConnectionManager.setDefaultMaxPerRoute(1000);

		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		httpClientBuilder.setConnectionManager(pollingConnectionManager);
		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2,
				true));
		httpClientBuilder
				.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
		//
		List<Header> headers = new ArrayList<Header>();
		headers.add(new BasicHeader("Connection", "Keep-Alive"));
		//
		httpClientBuilder.setDefaultHeaders(headers);
		// proxy
		if (proxyEnabled) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort, "http");
			httpClientBuilder.setProxy(proxy);
		}
		//
		HttpClient httpClient = httpClientBuilder.build();
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
				httpClient);	
		clientHttpRequestFactory.setConnectTimeout(30000);
		clientHttpRequestFactory.setReadTimeout(30000);
		clientHttpRequestFactory.setConnectionRequestTimeout(30000);
		restTemplate.setRequestFactory(clientHttpRequestFactory);
	}
	
	
	public ResponseEntity<String> sendRequest(String url, HttpMethod method, HttpHeaders headers)
			throws HttpClientErrorException, HttpServerErrorException, Exception{
		return sendRequest(url, method, headers, new Object());
	}
	
	public <T> ResponseEntity<String> sendRequest(String url, HttpMethod method, HttpHeaders headers, T requestBody) 
			throws HttpClientErrorException, HttpServerErrorException, Exception{
		
		HttpEntity<T> entity = new HttpEntity<T>(requestBody, headers);
		ResponseEntity<String> result = restTemplate.exchange(url,	method, entity, String.class);		
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private <T> T responseHttpError(Exception e) {
		if (!StringUtil.isNullOrEmpty(e.getMessage())) {
			return (T) StringUtil.writeJSON((new GeneralFailResponse(e
					.getMessage())));
		}
		return (T) StringUtil.writeJSON((new GeneralFailResponse()));
	}
}