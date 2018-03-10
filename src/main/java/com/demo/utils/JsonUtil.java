package com.demo.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static transient final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    public static final ObjectMapper JSON_MAPPER_CASE_INSENSITIVE = new ObjectMapper();

    static {
        JSON_MAPPER_CASE_INSENSITIVE.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public static <T> T readJSON(String jsonString, Class<T> clazz) {
        if (jsonString != null) {
            try {
                return JSON_MAPPER.readValue(jsonString, clazz);
            } catch (Exception ex) { // ignore ex
            	LOGGER.warn("readJSON error", ex);
            }
        }
        return null;
    }

    public static <T> T readJSON(InputStream in, Class<T> clazz) {
        if (in != null) {
            try {
                return JSON_MAPPER.readValue(in, clazz);
            } catch (Exception ex) {
            	LOGGER.warn("readJSON error", ex);
            }
        }
        return null;
    }

    public static <T> T readJSON(File in, Class<T> clazz) {
        if (in != null) {
            try {
                return JSON_MAPPER.readValue(in, clazz);
            } catch (Exception ex) {
            	LOGGER.warn("readJSON error", ex);
            }
        }
        return null;
    }

    public static <T> T readJSONIgnoreCase(String jsonString, Class<T> clazz) {
        if (jsonString != null) {
            try {
                return JSON_MAPPER_CASE_INSENSITIVE.readValue(jsonString, clazz);
            } catch (Exception ex) {
            	LOGGER.warn("readJSON error", ex);
            }
        }
        return null;
    }

    public static <T> T readJSONQuietly(String jsonString, Class<T> clazz) {
        if (jsonString != null) {
            try {
                return JSON_MAPPER.readValue(jsonString, clazz);
            } catch (Exception ex) { // ignore ex
            }
        }
        return null;
    }

    public static String writeJSON(Object object) {
        try {
            return JSON_MAPPER.writeValueAsString(object);
        } catch (Exception ex) {
        	LOGGER.warn("writeJSON error", ex);
        }
        return "";
    }

    public static void writeJSON(OutputStream out, Object object) throws IOException {
        JSON_MAPPER.writeValue(out, object);
    }

    public static String writeJSONQuietly(Object object) {
        try {
            return JSON_MAPPER.writeValueAsString(object);
        } catch (Exception ex) { // ignore ex
        }
        return "";
    }

    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        return JSON_MAPPER.convertValue(fromValue, toValueType);
    }

}
