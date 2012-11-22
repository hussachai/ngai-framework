package com.siberhus.ngai.fckeditor;

import javax.servlet.http.HttpServletRequest;

import net.fckeditor.requestcycle.UserPathBuilder;

public class UserIdPathBuilder implements UserPathBuilder {
	
	
	@Override
	public String getUserFilesAbsolutePath(HttpServletRequest request) {
		
		return "D:/temp/userfiles"+"/tuey";
	}
	
	@Override
	public String getUserFilesPath(HttpServletRequest request) {
		String path = "/fileserver"+"/tuey";
		return path;
	}
	
}
