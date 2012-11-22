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
package com.siberhus.ngai;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.TemporalType;

/**
 * 
 * @see com.siberhus.ngai.QTO
 * @author hussachai
 * @since 0.9
 * 
 */
public class TemporalObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date temporalObject;
	private TemporalType temporalType;
	
	public TemporalObject(Date temporalObject, TemporalType temporalType){
		this.temporalObject = temporalObject;
		this.temporalType = temporalType;
	}
	
	public Date getTemporalObject() {
		return temporalObject;
	}
	public void setTemporalObject(Date temporalObject) {
		this.temporalObject = temporalObject;
	}
	public TemporalType getTemporalType() {
		return temporalType;
	}
	public void setTemporalType(TemporalType temporalType) {
		this.temporalType = temporalType;
	}
	
	
}
