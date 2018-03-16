package com.demo.utils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

//import org.codehaus.jackson.map.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.Gson;

public class StringUtil3 {


	public static boolean isNullOrEmpty(String str) {
		return str == null ? true : str.isEmpty();
	}
	
	
	public static boolean isNullOrEmpty(Object obj) {
		return obj == null ? true : obj.toString().isEmpty();
	}
	
	public static long nowUTCTimestamp() {
		TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");
		return Calendar.getInstance(utcTimeZone).getTimeInMillis();
	}
	
	public static String convertTimestampLongToString(long timestamp) {
		return String.format("%015d", timestamp);
	}
	
	
	
	public static String convertTimestampToMessageIdFormat(String timestamp) {
		return String.format("%019d", timestamp + "0000");
	}
	
	public static String convertTimestampToMessageIdFormat(long timestamp) {
		return String.format("%019d", timestamp + "0000");
	}
		
	public static String convertMessageIdToTimestampFormat(String messageId) {
		return messageId.substring(2, 15);
	}
	
	public static long convertMessageIdToTimestampLongFormat(String messageId) {
		return Long.parseLong(convertMessageIdToTimestampFormat(messageId));
	}
	
	public static String convertMessageIdToDbTimestampFormat(String messageId) {
		return messageId.substring(0, 15);
	}
		
	public static Map<Long, String> convertObjToSpecificLSMap(Object obj) { 
		Map<Long, String> map = new HashMap();
		ObjectMapper mapper = new ObjectMapper();
		String str = null;
		try {
			str = mapper.writeValueAsString(obj);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (str != null) {
			try {
				Map<String, Object> tmp = mapper.readValue(str, Map.class);
				for(String key : tmp.keySet()) {
					map.put(Long.parseLong(key), tmp.get(key).toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	    return map;
	}
	
	
	/*
	 * json string to object
	 * 
	 */
	private static Gson gson;
	
	private static Gson getGson(){
		if(gson == null){
			gson = new Gson();
		}
		return gson;
	}
	public static <T> T readJSON(String jsonString, Class<T> clazz) {
		return readJSON(jsonString, clazz, true);
	}
	
	public static <T> T readJSON(String jsonString, Class<T> clazz, boolean printStackTrace) {
		try {
			return getGson().fromJson(jsonString, clazz);
		} catch (Exception e) {			
			if(printStackTrace) {
				System.out.print(String.format("error \njson String:", jsonString));
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static <T> T readJSON(String jsonString, Type type) {
		return readJSON(jsonString, type, true);
	}	
	
	public static <T> T readJSON(String jsonString, Type type, boolean printStackTrace) {
		try {
			return getGson().fromJson(jsonString, type);
		} catch (Exception e) {			
			if(printStackTrace) {
				System.out.print(String.format("error \njson String:", jsonString));
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/*
	 * object to json string
	 * 
	 */
	public static String writeJSON(Object object) {
		try {
			return getGson().toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String writeJSON(Object object, Type type) {
		try {
			return getGson().toJson(object, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	

	//	//待研究
//	public static Map<String, Object> introspect(Object obj) throws Exception {
//	    Map<String, Object> result = new HashMap<String, Object>();
//	    BeanInfo info = Introspector.getBeanInfo(obj.getClass());
//	    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
//	        Method reader = pd.getReadMethod();
//	        if (reader != null)
//	            result.put(pd.getName(), reader.invoke(obj));
//	    }
//	    return result;
//	}
	
	public static long checkTimestamp(String timestamp) throws NumberFormatException {
		
		if (timestamp == null || !timestamp.matches("\\d{13}")) {
			throw new NumberFormatException("parse error");
		}
		return Long.parseLong(timestamp);
	}
	
//	private long parseTimestampFromIqQueryElement(String queryElementContent) throws NumberFormatException {
//
//		if (queryElementContent == null
//				|| !queryElementContent
//						.matches("\\d{4}-[01]\\d-[0-3]\\d [0-2]\\d:[0-5]\\d")) {
//			throw new NumberFormatException("parse error");
//		}
//
//		SimpleDateFormat sdf = new SimpleDateFormat(
//				"yyyy-MM-dd HH:mm");
//		Date date = null;
//		try {
//			date = sdf.parse(queryElementContent);
//		} catch (ParseException e) {
//			throw new NumberFormatException("parse error");
//		}
//		return date.getTime();
//	}
	 
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey( Map<K, V> map)
	{
	    List<Map.Entry<K, V>> list =
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	        	return (o1.getKey()).compareTo( o2.getKey() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}    
    
    public static <K , V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map)
	{
	    List<Map.Entry<K, V>> list =
	        new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>()
	    {
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
	        {
	        	return (o1.getValue()).compareTo( o2.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list)
	    {
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}    
}
