package com.demo.cache.anno2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface CachePut {

	String key();

	String keys() default "";

	String[] cacheNames();

	String cacheManager();

	String unless() default "";

	boolean ignoreCase() default true;

	String dbId() default "1";
	
	boolean hashFlag() default false;

	/**
	 * 本地cache manager
	 * 
	 * @return
	 */
	String localCacheManager() default "";
}
