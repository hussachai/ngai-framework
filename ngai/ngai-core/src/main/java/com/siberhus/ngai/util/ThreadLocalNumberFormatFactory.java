package com.siberhus.ngai.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadLocalNumberFormatFactory {

	private static final ThreadLocal<Map<String, NumberFormat>> threadLocal = new ThreadLocal<Map<String, NumberFormat>>() {
		@Override
		protected Map<String, NumberFormat> initialValue() {
			return new ConcurrentHashMap<String, NumberFormat>();
		}
	};
	
	public static NumberFormat getInstance(String pattern) {
		Map<String, NumberFormat> cache = threadLocal.get();
		NumberFormat df = cache.get(pattern);
		if (df == null) {
			df = new DecimalFormat(pattern);
			cache.put(pattern, df);
		}
		return df;
	}
	
}
