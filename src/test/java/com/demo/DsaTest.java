package com.demo;

import static org.junit.Assert.*;

import org.junit.Test;

import com.demo.utils.CollectionUtil;

import java.util.*;

public class DsaTest {

	@Test
	public void set() {
		Set set = new HashSet();
		set.add("Bernadine");
		set.add("Elizabeth");
		set.add("Gene");
		set.add("Elizabeth");
		set.add("Clara");
		System.out.println(set);

		Set sortedSet = new TreeSet(set);
		System.out.println(sortedSet);
	}

	@Test
	public void map() {
//		Map map = new HashMap();
		HashMap<String, String> map = new HashMap<String, String>();
	    map.put("apple", "苹果");
	    map.put("watermelon", "西瓜");
	    map.put("banana", "香蕉");
	    map.put("peach", "桃子");
	    	    
	    System.out.println("HashMap");
//		for(Map.Entry<Integer, String> entry : map.entrySet()) {
//		    int key = entry.getKey();
//		    String value = entry.getValue();
//
//		    System.out.println(key + ":" + value);
//		}
	    Iterator iter = map.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry entry = (Map.Entry) iter.next();
	        System.out.println(entry.getKey() + "=" + entry.getValue());
	    }	
	    
	    LinkedHashMap<String, String> linkMap = new LinkedHashMap<String, String>();
	    linkMap.put("apple", "苹果");
	    linkMap.put("watermelon", "西瓜");
	    linkMap.put("banana", "香蕉");
	    linkMap.put("peach", "桃子");
	    
	    System.out.println("LinkedHashMap");
	    iter = linkMap.entrySet().iterator();
	    while (iter.hasNext()) {
	        Map.Entry entry = (Map.Entry) iter.next();
	        System.out.println(entry.getKey() + "=" + entry.getValue());
	    }
		
//		Map sortedMap = new TreeMap(map);
		TreeMap<String, String> sortedMap = new TreeMap<String, String>(map);
		System.out.println("TreeMap");
		for(Map.Entry<String, String> entry : sortedMap.entrySet()) {
			String key = entry.getKey();
		    String value = entry.getValue();

		    System.out.println(key + ":" + value);
		}	
	}
	
	@Test
	public void weakMap() {
		WeakHashMap<Object, String> weakMap = new WeakHashMap<Object, String>();
		Object someDataObject = new Object();
		weakMap.put(someDataObject, "test");
		System.out.println("map contains someDataObject ? " + weakMap.containsKey(someDataObject));
	 
	// -- now make someDataObject elligible for garbage collection...
		someDataObject = null;
		for (int i = 0; i < 100000; i++) {
			if (weakMap.size() != 0) {
				System.out.println("At iteration " + i + " the map still holds the reference on someDataObject");
			} else {
				System.out.println("WeakHashMap has finally been garbage collected at iteration " + i + ", hence the map is now empty");
				break;
			}
		}
	}	

	@Test
	public void list() {
		List<String> list = new ArrayList<String>();
		int size = CollectionUtil.size(list);
		System.out.println(size);

		list.add("test");
		size = CollectionUtil.size(list);
		System.out.println(size);
		System.out.println(list);
	}

}
