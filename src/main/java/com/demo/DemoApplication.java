package com.demo;

import java.util.ArrayList;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

import com.demo.dao.mysql.base.BasicDataSourceArrayFactoryBean;
import com.demo.dao.mysql.base.LazyConnectionDataSourceProxyArrayFactoryBean;
import com.demo.dao.mysql.base.NamedParameterJdbcTemplateArrayFactoryBean;
import com.demo.utils.ArrayUtil;
import com.demo.utils.CollectionUtil;


@SpringBootApplication
//@TestPropertySource({
//	"file:src/main/resources/test.properties",
//	"file:src/main/resources/system.properties",
//	"file:src/main/resources/application.properties"
//})
@ImportResource({
	"file:src/main/resources/spring-context.xml",
	"file:src/main/resources/spring-cache.xml",
	"file:src/main/resources/spring-data.xml",
	"file:src/main/resources/spring-test.xml"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
//	@Bean
//	public RestTemplate restTemplate() {
//	    return new RestTemplate();
//	}

	
//    @Bean
//    public org.apache.commons.dbcp2.BasicDataSource shardingDataSourceTarget(){
//    	org.apache.commons.dbcp2.BasicDataSource ds = new org.apache.commons.dbcp2.BasicDataSource();
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        ds.setConnectionProperties("useUnicode=yes;characterEncoding=UTF-8;pinGlobalTxToPhysicalConnection=true;autoReconnect=true;useSSL=false");
//        ds.setUrl("jdbc:mysql://10.16.179.194:3306/test1");
//        ds.setUsername("chat");
//        ds.setPassword("Newegg@123");
//        
//        return ds;
//    }	
//	
//    @Bean
//    public org.apache.commons.dbcp2.BasicDataSource shardingDataSource2Target(){
//    	org.apache.commons.dbcp2.BasicDataSource ds = new org.apache.commons.dbcp2.BasicDataSource();
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        ds.setConnectionProperties("useUnicode=yes;characterEncoding=UTF-8;pinGlobalTxToPhysicalConnection=true;autoReconnect=true;useSSL=false");
//        ds.setUrl("jdbc:mysql://10.16.179.194:3306/test2");
//        ds.setUsername("chat");
//        ds.setPassword("Newegg@123");
//        return ds;
//    }
	
//  @Bean
//  public org.apache.commons.dbcp2.BasicDataSource shardingDataSource3Target(){
//  	  BasicDataSource ds = new BasicDataSource();
//      ds.setDriverClassName("com.mysql.jdbc.Driver");
//      ds.setConnectionProperties("useUnicode=yes;characterEncoding=UTF-8;pinGlobalTxToPhysicalConnection=true;autoReconnect=true;useSSL=false");
//      ds.setUrl("jdbc:mysql://10.16.179.194:3306/test2");
//      ds.setUsername("chat");
//      ds.setPassword("Newegg@123");
//      
//      ArrayList<BasicDataSource> list = new ArrayList<BasicDataSource>();
//      list.add(ds);
//      BasicDataSource[] array = list.toArray(new BasicDataSource[0]);
//      
//      BasicDataSourceArrayFactoryBean shardingDataSourceArrayFactoryBeanTarget = new BasicDataSourceArrayFactoryBean();
//      
//      shardingDataSourceArrayFactoryBeanTarget.setBasicDataSourceArray(array);
//      
//      LazyConnectionDataSourceProxyArrayFactoryBean shardingDataSourceArrayFactoryBean = new LazyConnectionDataSourceProxyArrayFactoryBean();
//      shardingDataSourceArrayFactoryBean.setTargetDataSources(array);
//      
//      NamedParameterJdbcTemplateArrayFactoryBean shardingJdbcTemplateArrayFactoryBean = new NamedParameterJdbcTemplateArrayFactoryBean(array);
//      
//      return ds;
//  }	
}
