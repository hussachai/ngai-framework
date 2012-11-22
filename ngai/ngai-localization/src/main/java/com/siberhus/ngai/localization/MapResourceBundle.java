package com.siberhus.ngai.localization;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

public class MapResourceBundle extends ResourceBundle {
	
	private Map<String, String> messages;
	private Enumeration<String> keys;

	public MapResourceBundle(Map<String, String> messages) {
		this.messages = messages;
		this.keys = Collections.enumeration(messages.keySet());
	}
	
	@Override
	public Enumeration<String> getKeys() {
		return keys;
	}

	@Override
	protected Object handleGetObject(String key) {
		return messages.get(key);
	}
	
	public void put(String key, String value){
		if(messages.containsKey(key)){
			messages.put(key, value);
		}
	}
	
}