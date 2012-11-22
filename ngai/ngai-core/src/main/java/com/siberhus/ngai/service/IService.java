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
package com.siberhus.ngai.service;


/**
 * <p>
 * This is just marker interface. You must implement this interface if you would like to enable <br/>
 * automatic resource injection for all attributes that was marked with annotation @PersistenceUnit,<br/>
 * <code>@PersistenceContext</code>, and @StripesService. </br>
 * </p>
 * <p>
 * In action bean class support only @StripesService annotation because I think it's better to prevent<br/>
 * somebody puts everything into action bean. I guest you may agree with me. Sorry for the guy who always<br/>
 * make one stop class. I think I make their life a little harder. Sorry!.<br/>
 * </p>
 * <p>
 * Service class support all annotations. You can mark the attribute(field|method) with @PersistenceContext, <br/>
 * if you'd like to use EntityManager and if you'd like EntityManagerFactory you must mark the attribute<br/>
 * with @PersistenceUnit. For example:<br/>
 * </p>
 * 
 * <pre>
 *  <code>@PersistenceContext</code>
 *  private EntityManager entityManager1;//default persistence context
 *  
 *  <code>@PersistenceContext(unitName="foo")</code>
 *  private EntityManager entityManager2;
 * </pre>
 * Each Service class may refer to another service by annotating attribute with @StripesService annotation<br/>
 * then Stripersistence will inject that service with required dependencies to the declared service bean. 
 * 
 * @author hussachai
 * @since 0.9
 */
public interface IService {}
