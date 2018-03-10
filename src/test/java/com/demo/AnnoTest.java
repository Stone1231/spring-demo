package com.demo;

//import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.demo.model.TestModel;
import com.demo.service.AnnoTestService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AnnoTest {

	@Autowired
	private AnnoTestService service;
	
	@Test
	public void test() {
		TestModel model = new TestModel();
		model.setUserName("user name");
		model.setUserId("ID");
		model.setNickName("dog");
		
		Map<String, Object> map = null; 
		try{
			map = service.getAnnoFields(model.getClass(), model);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO: handle exception
		}
		
		System.out.println(map.get("nick_name").toString());
		
		assertTrue(map != null && map.size() > 0);
		assertTrue(map.get("nick_name").equals("dog"));		
	}

}
