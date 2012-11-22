/* Copyright 2009 Hussachai Puripunpinyo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.siberhus.ngai.action;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author hussachai
 *
 */
public class DefaultUserAware implements IUserAware<Serializable>{
	
	public static final String USER_ATTRIBUTE_NAME = "USER";
	
	private static IUserAware<Serializable> instance;
	
	public synchronized static IUserAware<Serializable> getInstance(){
		if(instance==null){
			instance = new DefaultUserAware();
		}
		return instance;
	}
	
	@Override
	public Serializable getUser(HttpServletRequest request) {
		Serializable user = (Serializable)request
			.getSession().getAttribute(USER_ATTRIBUTE_NAME);
		return user;
	}
	
}
