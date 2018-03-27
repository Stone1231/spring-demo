package com.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.test.context.junit4.SpringRunner;
import com.demo.base.BaseTest;
import com.demo.dao.mysql.base.BasicDataSourceArrayFactoryBean;
import com.demo.dao.mysql.base.NamedParameterJdbcTemplateArrayFactoryBean;
import com.demo.model.TestList;
import com.demo.model.TestModel;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BeanTest extends BaseTest {

	@Autowired
	private TestModel testModel;
	
	@Autowired
	private TestModel testModel2;
	
	@Autowired
	@Qualifier("testModel")
	private TestModel model;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private TestList constructorChilds;
	
	@Test
	public void testAbstract() {

		TestModel model = applicationContext.getBean("child1",
				TestModel.class);
		
		
		TestList list = applicationContext.getBean("childs",
				TestList.class);
		
		
		System.out.println(model.getUserName());
		
		System.out.println(list.getData().get(1).getNickName());
	}	
	
	@Test
	public void testContext() {

		LazyConnectionDataSourceProxy[] dataSources = applicationContext.getBean("shardingDataSourceArrayFactoryBean",
				LazyConnectionDataSourceProxy[].class);
		
		NamedParameterJdbcTemplateArrayFactoryBean[] shardingJdbcTemplateArrayFactoryBean = applicationContext.getBean("shardingJdbcTemplateArrayFactoryBean",
				NamedParameterJdbcTemplateArrayFactoryBean[].class);
		
		TestModel model = applicationContext.getBean("testModel",
				TestModel.class);
		
		System.out.println(model.getUserName());
	}
	
	@Test
	public void testBasicDataSource() {

		org.apache.commons.dbcp2.BasicDataSource dataSource= applicationContext.getBean("shardingDataSource2Target",
				org.apache.commons.dbcp2.BasicDataSource.class);
		
		
		System.out.println(dataSource.getUsername());
	}	
	
	@Test
	public void test() {

		System.out.println(testModel.getUserName());
		
		System.out.println(testModel2.getUserName());
		
		System.out.println(model.getUserName());
		
		System.out.println(constructorChilds.getData().get(0).getUserName());
		
	}

}
