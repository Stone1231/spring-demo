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
}
