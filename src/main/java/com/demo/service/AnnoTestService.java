package com.demo.service;

import java.util.Map;

public interface AnnoTestService {
	Map<String, Object> getAnnoFields(Class<?> clazz, Object obj) throws IllegalArgumentException, IllegalAccessException;
}
