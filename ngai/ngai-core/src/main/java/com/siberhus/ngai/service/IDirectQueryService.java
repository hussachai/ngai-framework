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

import com.siberhus.ngai.EntityQueryMap;

/**
 * 
 * <p>
 * The purpose of this service is only provide data for the view. You may use this service for the business logic <br/>
 * by passing query string to the getEntityQueryMap().get(queryString) and get the result back but it's not recommended.<br/>
 * I don't want to mess the custom service code with the logic that is used only in view layer that is the reason why <br/>
 * I created this service.<br/>
 * </p>
 * <p>
 * In JSP code you can query all entities by putting the entity name to the map. Thanks for EL that make our life easier<br/>
 * and make our code more beautiful. If you don't believe that. Look the example below. <br/>
 * </p>
 * <pre>
 * &lt;s:select name="model.merchant.id"&gt;
 *     &lt;s:options-collection collection="${actionBean.entityListMap.Merchant}" label="name" value="id"/&gt;
 * &lt;/s:select&gt;
 * </pre>
 * Another example when you would like to query with condition use entityQueryMap instead.<br/>
 * For example:<br/>
 * <pre>
 * &lt;s:select name="model.merchant.id"&gt;
 *     &lt;s:options-collection collection="${actionBean.entityQueryMap['from Merchant m where m=1']}" label="name" value="id"/&gt;
 * &lt;/s:select&gt;
 * </pre>
 * That's very cool! isn't it?<br/><br/>
 * 
 * <strong>
 * Remember that this service is intentionally for view layer.
 * </strong>
 * 
 * @author hussachai
 * @since 0.9
 */
public interface IDirectQueryService extends IService {
	
	public EntityQueryMap getEntityQueryMap();
}
