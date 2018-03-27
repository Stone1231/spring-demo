package com.demo;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.demo.base.BaseTest;
import com.demo.service.QueueTestService;

@RunWith(SpringRunner.class)
@SpringBootTest
// @ContextConfiguration({
// "file:src/main/webapp/WEB-INF/test-spring-context.xml",
// "file:src/main/webapp/WEB-INF/spring-data.xml" })
// @TestPropertySource("file:src/main/webapp/WEB-INF/config.properties")
public class QueueTest extends BaseTest {
	
//	@Rule
//	public BenchmarkRule benchmarkRule = new BenchmarkRule();	
//	
	@Autowired
	private QueueTestService service;
	
//	@Test
//	@BenchmarkOptions(benchmarkRounds = 5, warmupRounds = 0, concurrency = 1)
//	public void hi()  {
//		System.out.print("hi man \n");
//	}
	
	@Test
	public void sendData() {
		for (int i = 0; i < 5; i++) {
			service.sendData(i);
		}
	}
	
	@Test
	public void sendDataSync() throws InterruptedException  {
		for (int i = 0; i < 5; i++) {
			service.sendDataSync(i);
		}
		
		Thread.sleep(20000L);
	}
}
