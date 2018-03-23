package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@ImportResource({
	"file:src/main/resources/spring-context.xml",
	//"file:src/main/resources/spring-data.xml",
	"file:src/main/resources/spring-test.xml"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
//	@Bean
//	public RestTemplate restTemplate() {
//	    return new RestTemplate();
//	}
}
