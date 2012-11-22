package com.siberhus.ngai.resource;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import com.siberhus.commons.util.ResourceUtils;
import com.siberhus.ngai.core.IResourceFactory;

public class PropertyResourceFactory implements IResourceFactory {

	@Override
	public Object createResource(String[] args) {
		Properties properties = new Properties();

		for (String arg : args) {
			InputStream is = null;
			try {
				is = ResourceUtils.openInputStream(arg);
				properties.load(is);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}

		return properties;
	}
	
	public static void main(String[] args) {
		IResourceFactory irf = new PropertyResourceFactory();
		Properties props = (Properties)irf.createResource(new String[]{"tmp/a.txt","tmp/b.txt"});
		System.out.println(props);
	}
	
}
