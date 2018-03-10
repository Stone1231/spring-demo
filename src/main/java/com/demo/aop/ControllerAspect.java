package com.demo.aop;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import com.demo.model.TestModel;

@Aspect
@Component
public class ControllerAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

	@Autowired
	private HttpServletRequest request;

	@SuppressWarnings("unchecked")
	@Around("execution(public * com.demo.controller.*.*(..)) && args(requestBaseObj, ..)")
	public Object controllerAround(ProceedingJoinPoint proceedingJoinPoint, Object requestBaseObj) throws Throwable {
		if (requestBaseObj instanceof TestModel) {
			TestModel requestBase = (TestModel) requestBaseObj;
			// checkData(requestBase);
			LOGGER.info("controllerAround:" + requestBase.getUserName());
			return proceedingJoinPoint.proceed();
		} else if (requestBaseObj instanceof Object) {
			String requestUrl = request.getRequestURI();
			if (requestUrl.contains("/someUrl")) {
				return proceedingJoinPoint.proceed();
			}
			String token = "token";// fetchToken(request);
			if (token != null) {
				return proceedingJoinPoint.proceed();
			}
		}
		LOGGER.error("aspect error, must specify at least one param.");
		throw new HttpServerErrorException(HttpStatus.BAD_REQUEST);
	}
}
