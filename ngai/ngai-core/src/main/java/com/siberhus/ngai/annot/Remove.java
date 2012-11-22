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
package com.siberhus.ngai.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Applied to a business method of a session-scoped bean class. Indicates that the session-scoped bean is<br/> 
 * to be removed by the container after completion of the method. <br/>
 * </p>
 * 
 * @author hussachai
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Remove {
	
	/**
	 * <p>
	 * If true, the session-scoped bean will not be removed if an exception is thrown from the designated method.<br/>
	 * </p>
	 * @return
	 */
	boolean retainIfException () default false;
	
}
