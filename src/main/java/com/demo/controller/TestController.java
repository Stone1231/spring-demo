package com.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.demo.aop.ServiceAspect;
import com.demo.model.TestModel;

@RestController
public class TestController extends AbstractController {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String Test(){
		
		LOGGER.info("hello world");
		
		return "hello world";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.POST)
	public TestModel TestPost(@RequestBody TestModel model){

		return model;
	}
}
