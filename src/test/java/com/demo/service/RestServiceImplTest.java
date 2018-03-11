package com.demo.service;

import static org.junit.Assert.*;

import java.sql.Time;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.demo.DemoApplication;
import com.demo.service.RestService;
import com.demo.utils.DateUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RestServiceImplTest {
	
	@Autowired
	private RestService service;
	
	@Test
	public void test() {
		
		assertTrue(false);
		
//		String response = service.sendPostRequestForObj("url", new HttpHeaders(),
//				new Object(), String.class);
		
//		try {
//			service.sendRequest("url", HttpMethod.POST, new HttpHeaders(), new Object());
//		} catch (HttpClientErrorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (HttpServerErrorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
