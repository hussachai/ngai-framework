package com.siberhus.ngai.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ThreadLocalDateFormatFactory {
	
	private static final ThreadLocal<Map<String, DateFormat>> threadLocal 
		= new ThreadLocal<Map<String, DateFormat>>(){
			@Override
			protected Map<String, DateFormat> initialValue() {
				return new ConcurrentHashMap<String, DateFormat>();
			}
	};
	
	public static DateFormat getInstance(String pattern, Locale locale){
		Map<String, DateFormat> cache = threadLocal.get();
		String key = pattern+locale;
		DateFormat df = cache.get(key);
		if(df==null){
			df = new SimpleDateFormat(pattern, locale);
			cache.put(key, df);
		}
		return df;
	}
	
}
