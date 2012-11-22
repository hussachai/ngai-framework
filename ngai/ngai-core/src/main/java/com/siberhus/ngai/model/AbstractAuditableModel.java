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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class AbstractAuditableModel<ID extends Serializable, USER extends IModel<ID>>
		extends AbstractModel<ID> implements
		IAuditableModel<ID,USER> {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name="CREATED_BY",referencedColumnName="ID")
	private USER createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATED_AT")
	private Date createdAt;
	
	@ManyToOne
	@JoinColumn(name="LAST_MODIFIED_BY",referencedColumnName="ID")
	private USER lastModifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_MODIFIED_AT")
	private Date lastModifiedAt;
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public USER getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(USER createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public USER getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(USER lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	
	
}
