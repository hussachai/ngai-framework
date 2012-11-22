package com.siberhus.ngai.util;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.siberhus.ngai.config.CoreConfig;

public class DefaultDataFormat {
	
	private static final Map<String, DateFormat> dateFormatMap 
		= new ConcurrentHashMap<String, DateFormat>();
	private static final Map<String, DecimalFormat> numberFormatMap 
		= new ConcurrentHashMap<String, DecimalFormat>();
	
	public static String formatDate(Date date){
		if(date==null){
			return null;
		}
		String typeName = date.getClass().getName();
		DateFormat dateFormat = getDateFormat(typeName);
		synchronized(dateFormat){
			return dateFormat.format(date);
		}
	}
	
	public static String formatNumber(Number number){
		if(number==null){
			return null;
		}
		String typeName = number.getClass().getName();
		DecimalFormat numberFormat = getNumberFormat(typeName);
		synchronized(numberFormat){
			return numberFormat.format(number);
		}
	}
	
	public static Date parseDate(String data, String typeName) throws ParseException{
		DateFormat dateFormat = getDateFormat(typeName);
		synchronized(dateFormat){
			return (Date)dateFormat.parseObject(data);
		}
	}
	
	public static Number parseNumber(String data, String typeName) throws ParseException{
		DecimalFormat numberFormat = getNumberFormat(typeName);
		synchronized(numberFormat){
			return (Number)numberFormat.parse(data);
		}
	}
	
	private static DateFormat getDateFormat(String typeName){
		DateFormat dateFormat = dateFormatMap.get(typeName);
		if(dateFormat==null){
			CoreConfig coreConfig = CoreConfig.get();
			dateFormat = new SimpleDateFormat(coreConfig.getFormatPattern()
					.get(typeName), coreConfig.getLocale());
			dateFormatMap.put(typeName, dateFormat);
		}
		return dateFormat;
	}
	
	private static DecimalFormat getNumberFormat(String typeName){
		DecimalFormat numberFormat = numberFormatMap.get(typeName);
		if(numberFormat==null){
			CoreConfig coreConfig = CoreConfig.get();
			numberFormat = new DecimalFormat(coreConfig
					.getFormatPattern().get(typeName));
			numberFormatMap.put(typeName, numberFormat);
		}
		return numberFormat;
	}
	
	public static void main(String[] args) {
		for(String c : (Set<String>)Charset.availableCharsets().keySet()){
			System.out.println(c);
		}
	}
	
}
