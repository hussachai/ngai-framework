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

import com.siberhus.ngai.dao.AbstractCrudDao;

/**
 * <p>
 * Interface marks class which can be enabled or disabled.<br/>
 * The entity which is marked with this interface cannot be deleted by {@link AbstractCrudDao}<br/>
 * but it will be hidden instead.<br/>
 * 
 * We return Boolean wrapper class instead of native boolean because<br/> 
 * we can retrieve all objects (both active and not-active) by skipping the null<br/>
 * active value and don't append SQL condition for active<br/> 
 * </p>
 * 
 * @author hussachai
 * @since 1.0
 */
public interface IUnremovableModel {
	
	public static final String FIELD_ACTIVE = "active";
	
	
	/**
     * Check if object is active.
     *
     * @return true when object is active
     */
	Boolean getActive();
    
    /**
     * Set object's active flag.
     *
     * @param isActive value of active flag
     */
    void setActive(Boolean isActive);
    
}
