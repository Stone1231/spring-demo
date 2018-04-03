package com.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component//實例化到spring容器中，相當於配置文件中的<bean id="" class=""/>
public class ServiceAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAspect.class);

	@Before("execution(public * com.demo.service.*.*(..))")
	public void logBefore(JoinPoint point) {
		LOGGER.info("Before：at " + point.getTarget().getClass());
		LOGGER.info("calling " + point.getSignature().getName());
		if (point.getArgs().length > 0) {
			for (Object item : point.getArgs()) {
				LOGGER.info(item.toString());
			}
		}
		LOGGER.info("before 結束，真正被呼叫的方法開始：");
	}

	@Around("execution(public * com.demo.service.*.*(..))")
	public Object logAround(ProceedingJoinPoint point) throws Throwable {
		LOGGER.info("Around：at " + point.getTarget().getClass());
		LOGGER.info("calling " + point.getSignature().getName());
		LOGGER.info("using " + point.getArgs()[0] + " ");
		Object result = point.proceed();
		LOGGER.info("Around：result=" + result);
		return result;
	}

	@AfterReturning(value = "execution(public * com.demo.service.*.*(..))", returning = "result") // returning="result"為必填
	public void logAfter(JoinPoint point, Object result) {
		LOGGER.info("After：at " + point.getTarget().getClass());
		LOGGER.info("After：result=" + result);
	}

	@AfterThrowing(value = "execution(public * com.demo.service.*.*(..))", throwing = "exc") // throwing="exc"為必填
	public void logThrow(JoinPoint point, Throwable exc) {
		LOGGER.info("AfterThrowing：" + exc);
	}
}
