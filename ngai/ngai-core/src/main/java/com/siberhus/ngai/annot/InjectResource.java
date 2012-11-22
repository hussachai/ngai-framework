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

import com.siberhus.ngai.core.IResourceFactory;

/**
 * 
 * @author hussachai
 *
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectResource {
	
	/**
	 * Specify ResourceFactoryClass
	 * @return
	 */
	Class<? extends IResourceFactory> factory();
	
	/**
	 * Specify the name if you want to share the resource across the application.
	 * If resource have the name it will be instantiate only once.
	 * Don't specify this value if the resource is not thread-safe such as java.sql.Connection.
	 * 
	 * @return
	 */
	String name() default "";
	
	/**
	 * Arguments to pass to the ResourceFactory
	 * @return
	 */
	String[] args() default "";
	
}
