package com.demo.base;

import org.junit.runner.RunWith;
//import org.powermock.core.classloader.annotations.PowerMockIgnore;
//import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

//import base.DirtiesContext;

//@RunWith(PowerMockRunner.class)
//@PowerMockIgnore("javax.management.*")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource({
	"file:src/main/resources/test.properties",
	"file:src/main/resources/system.properties",
	"file:src/main/resources/application.properties"
})
//@ImportResource({
//	"file:src/main/resources/spring-test.xml"}) //無效
public abstract class BaseTest {
}
