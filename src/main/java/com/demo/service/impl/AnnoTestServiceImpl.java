package com.demo.service.impl;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.demo.anno.AnnoField;
import com.demo.service.AnnoTestService;
import com.fasterxml.jackson.annotation.JsonProperty;

@Service
public class AnnoTestServiceImpl implements AnnoTestService {
	
    public Map<String, Object> getAnnoFields(Class<?> clazz, Object obj)
            throws IllegalArgumentException, IllegalAccessException {
        if (clazz == null) {
            return null;
        }
        
        Map<String, Object> map = new TreeMap<>();
        
        //getAnnoFields(clazz, obj);
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(AnnoField.class)) {
                continue;
            }
            field.setAccessible(true);
            String serializeName = field.getName();
            JsonProperty jsonPoperty = field.getAnnotation(JsonProperty.class);
            if (jsonPoperty != null) {
                serializeName = jsonPoperty.value();
            }
            map.put(serializeName, field.get(obj));
        }
        
        return map;
    }
}
