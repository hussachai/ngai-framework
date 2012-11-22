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
package com.siberhus.ngai.dao;

import com.siberhus.ngai.annot.JpaNativeQuery;
import com.siberhus.ngai.annot.JpaQuery;

/**
 * <p>
 * This is marker interface for DAO. It extends from {@link IDao}, so the subclass/subinterface does not need to implement {@link IDao} interface </br>
 * Implements this interface if you would like to use the advance feature of INgai's DAO system. All implemented classes or extended interfaces <br/>
 * can have the special methods that are annotated with {@link JpaQuery} or {@link JpaNativeQuery}. These methods can be defined in interface<br/>
 * or concrete class, but these methods don't have to be implemented for interface or don't have to add any logic for concrete class.<br/>
 * Ngai DAO system will implement all of these methods for you.
 * 
 * </p>
 * @author hussachai
 * @since 0.9
 */
public interface IAnnotatedQueryDao extends IDao{}


