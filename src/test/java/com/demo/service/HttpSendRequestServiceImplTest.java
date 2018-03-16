package com.demo.service;

import javax.annotation.Resource;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.model.Feeback;
import com.demo.model.Message;
import com.demo.model.MessageRequest;
import com.demo.utils.StringUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HttpSendRequestServiceImplTest {

	@Rule
	public BenchmarkRule benchmarkRule = new BenchmarkRule();
	
	@Resource
	HttpSendRequestService service;
	
	@Test
	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0, concurrency = 1)
	public void callService() throws Exception {
		
		String url ="http://localhost/someapi";	
		MessageRequest request = new MessageRequest();
		request.setType("type");
		request.setLogDate(0L);

		HttpHeaders headers = new HttpHeaders();
		//headers.add("ServiceName", "someservice");
		
		Feeback feeback = service.callService(String.format("%s%s", url, "/someurl"), headers, request);
		System.out.print(String.format("API: %s%s \n", url, "/someurl"));		
		System.out.print(String.format("Harder: %s \n", StringUtil.writeJSON(headers)));		
		
		Message message = StringUtil.readJSON(feeback.getMessage(), Message.class);
		
		System.out.print(String.format("Response: %s \n", StringUtil.writeJSON(message)));
	}
}
