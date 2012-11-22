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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * QTO is abbreviated from Query Transfer Object
 * 
 * <p>
 * This class is used for holding query string, parameters and result orders. It is used in {@link com.siberhus.ngai.action.AbstractCrudAction}<br/>
 * This class is serializable but it's not limit you from adding the non-serializable object as parameter.<br/>
 * </p>
 * <p>
 * If you'd like to add {@link java.util.Date} object, JPA will throw an exception because it never know which the date format<br/>
 * you'd like to keep in database. To solve this problem you must tell it what the exactly type of date such as<br/>
 * {@link java.sql.Date}, {@link java.sql.Timestamp}, or {@link java.sql.Time}. these type is extended from {@link java.util.Date}<br/>
 * You may choose to pass the sql specific date object but if you prefer to use {@link java.util.Date} you must encapsulate your date object<br/>
 * in the wrapper object {@link TemporalObject}. <br/>
 * </p>
 * <strong>Example</strong> <br/>
 * <pre>
 * java.lang.String name;
 * java.sql.Date date1;
 * java.util.Date date2;
 * ...
 * QTO qto = new QTO("from Customer m where m.name=? and m.dob between ? and ?");
 * .addParameter(name);
 * .addParameter(date1);
 * .addParameter(new TemporalObject(date2, TemporalType.DATE));
 * </pre>
 * <p>
 * You can get the result back from the service or DAO that is responsible for processing QTO. The result type almost be the List of entity.<br/>
 * If you'd like to manage the entity ordering. You can add {@link SortKey} object to QTO.<br/>
 * </p>
 * <strong>Example</strong> <br/>
 * <pre>
 * qto.addSortKey(new SortKey('m.name',SortKey.SortDir.DESC));
 * qto.addSortKey('m.name',SortKey.SortDir.ASC);
 * </pre>
 * 
 * @author hussachai
 * @since 0.9
 */
public class QTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private StringBuilder queryBuffer = new StringBuilder();
	
	private List<Object> parameterList = new ArrayList<Object>();
	
	private Set<SortKey> sortKeySet = new LinkedHashSet<SortKey>();
	
	private Integer firstResult = null;
	
	private Integer maxResult = null;
	
	private boolean nativeQuery = false;
	
	/**
	 * 
	 */
	public QTO(){}
	
	/**
	 * 
	 * @param queryString
	 */
	public QTO(String queryString){
		queryBuffer = new StringBuilder(queryString);
	}
	
	/**
	 * 
	 * @param queryString
	 * @param params
	 */
	public QTO(String queryString, Object... params){
		queryBuffer = new StringBuilder(queryString);
		if(params!=null){
			for(Object param : params){
				parameterList.add(param);
			}
		}
	}
	
	@Override
	public String toString(){
		return "{query: "+queryBuffer.toString()+", params: "+parameterList+
			", sortKeys: "+sortKeySet+", native: "+nativeQuery +" }";
	}
	
	public String buildQueryString(){
		String queryString = queryBuffer.toString();
		if(sortKeySet!=null && sortKeySet.size()!=0){
			queryString += " order by ";
			queryString += StringUtils.join(sortKeySet, ',');
//			for(SortKey sortKey : sortKeySet){
//				queryString += sortKey.toString();
//			}
		}
		return queryString;
	}
	
	public String getHashString(){
		String s = queryBuffer.toString()+ "|"
			+StringUtils.join(getParameterList(),',');
		return DigestUtils.md5Hex(s);
	}
	
	/**
	 * 
	 * @return queryString
	 */
	public String getQueryString() {
		return queryBuffer.toString();
	}
	
	public QTO setQueryString(String queryString){
		this.queryBuffer = new StringBuilder(queryString);
		return this;
	}
	
	/**
	 * 
	 * @param queryString
	 */
	public QTO appendQuery(String queryString){
		if(!queryString.endsWith(" ")){
			queryString+=" ";
		}
		queryBuffer.append(queryString);
		return this;
	}
	
	public boolean isNativeQuery() {
		return nativeQuery;
	}

	public QTO setNativeQuery(boolean nativeQuery) {
		this.nativeQuery = nativeQuery;
		return this;
	}

	/**
	 * 
	 * @return List of parameter
	 */
	public List<Object> getParameterList() {
		return parameterList;
	}
	
	/**
	 * 
	 * @param parameter
	 */
	public QTO addParameter(Object parameter){
		getParameterList().add(parameter);
		return this;
	}
	
	public QTO addParameters(Object... parameters){
		if(parameters!=null){
			for(Object parameter : parameters){
				getParameterList().add(parameter);
			}
		}
		return this;
	}
	
	public Set<SortKey> getSortKeySet(){
		return sortKeySet;
	}
	
	public QTO addSortKey(SortKey sortKey){
		getSortKeySet().add(sortKey);
		return this;
	}
	
	public QTO addSortKey(String key, SortKey.SortDir sortDir){
		getSortKeySet().add(new SortKey(key,sortDir));
		return this;
	}
	
	public boolean removeSortKey(SortKey sortKey){
		return getSortKeySet().remove(sortKey);
	}

	public Integer getFirstResult() {
		return firstResult;
	}

	public QTO setFirstResult(Integer firstResult) {
		this.firstResult = firstResult;
		return this;
	}

	public Integer getMaxResult() {
		return maxResult;
	}
	
	public QTO setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
		return this;
	}
	
}
