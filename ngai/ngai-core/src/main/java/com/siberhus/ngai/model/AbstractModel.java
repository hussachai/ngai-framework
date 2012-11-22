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
package com.siberhus.ngai.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.siberhus.ngai.util.DefaultDataFormat;


@MappedSuperclass
public abstract class AbstractModel<ID extends Serializable>
		implements IModel<ID> {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private ID id;
	
	public String[] toNameKeys(){
		return new String[0];
	}
	
	public String[] toNames(){
		return reflectToNames(getClass());
	}
	
	public Object[] toValues(){
		return reflectToValues(getClass(), this);
	}
	
	private static String[] reflectToNames(Class<?> entityClass){
		List<String> fieldNameList = new ArrayList<String>();
		for(Field field : entityClass.getDeclaredFields()){
			if(field.isAnnotationPresent(Column.class)){
				fieldNameList.add(field.getName());
			}
		}
		return fieldNameList.toArray(new String[0]);
	}
	
	private static Object[] reflectToValues(Class<?> entityClass, Object entity){
		List<Object> fieldValueList = new ArrayList<Object>();
		try{
			for(Field field : entityClass.getDeclaredFields()){
				if(field.isAnnotationPresent(Column.class)){
					if(!field.isAccessible()){
						field.setAccessible(true);
					}
					Object value = field.get(entity);
					if(value instanceof Date){
						value = DefaultDataFormat.formatDate((Date)value);
					}else if(value instanceof Number){
						value = DefaultDataFormat.formatNumber((Number)value);
					}
					fieldValueList.add(value);
				}
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return fieldValueList.toArray(new Object[0]);
	}
	
	public ID getId() {
		
		return id;
	}

	public void setId(final ID id) {

		this.id = id;
	}

	public boolean isNew() {
		return null == getId();
	}
	
	@Override
	public String toString() {

		return String.format("Entity of type %s with id: %s", this.getClass()
				.getName(), getId());
	}

	@Override
	public boolean equals(Object obj) {

		if (null == obj) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		if (!getClass().equals(obj.getClass())) {
			return false;
		}

		AbstractModel<?> that = (AbstractModel<?>) obj;

		return null == this.getId() ? false : this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {

		int hashCode = 17;

		hashCode += null == getId() ? 0 : getId().hashCode() * 31;

		return hashCode;
	}
}
