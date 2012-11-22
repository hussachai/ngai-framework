package com.siberhus.ngaiready.dev.action;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.siberhus.ngai.action.AbstractSimpleAction;


public abstract class BaseWebAttributeManagerAction extends AbstractSimpleAction {
	
	private int totalAttributesSize = 0;
	
	private List<WebAttribInfo> attribInfoList;
	
	@DefaultHandler
	@DontValidate
	public Resolution index() {
		populateAttributeInfoList();
		return new ForwardResolution("/pages/dev/web-attrib-manager.jsp");
	}
	
	public abstract String getTitle();
	
	public abstract Enumeration<?> getAttributeNames();
	
	public abstract Object getAttributeValue(String name);
	
	protected void populateAttributeInfoList(){
		attribInfoList = new ArrayList<WebAttribInfo>();
		Enumeration<?> attribNames = getAttributeNames();
		while(attribNames.hasMoreElements()){
			WebAttribInfo info = new WebAttribInfo();
			String attribName = (String)attribNames.nextElement();
			info.setName(attribName);
			Object attribValue = getAttributeValue(attribName);
			if(attribValue!=null){
				info.setType(attribValue.getClass().getName());
				int size = getObjectSize(attribValue);
				info.setSize(size);
				if(size>0){
					totalAttributesSize += info.getSize();
				}
			}else{
				info.setType("null");
				info.setSize(0);
			}
			attribInfoList.add(info);
		}
		
	}
	
	protected int getObjectSize(Object obj) {
		if (obj == null){
			return 0;
		}
		if( !(obj instanceof Serializable) ){
			return -1;
		}
		try{
			byte data[] = null;
			
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
			objOut.writeObject(obj);
			data = byteOut.toByteArray();
			return data.length;
		}catch(Exception e){
			e.printStackTrace();
			return -1;
		}
	}
	
	public int getTotalAttributesSize() {
		return totalAttributesSize;
	}

	public List<WebAttribInfo> getAttribInfoList() {
		return attribInfoList;
	}
	
}
