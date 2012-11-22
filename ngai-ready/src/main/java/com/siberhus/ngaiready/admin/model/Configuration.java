package com.siberhus.ngaiready.admin.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.siberhus.ngai.model.AbstractModel;


@Entity
@Table(name="CONFIGURAIONS")
public class Configuration extends AbstractModel<Long>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Column(name="CONFIG_NAME")
	private String name;
	
	@Column(name="CONFIG_GROUP")
	private String group;
	
	@Column(name="CONFIG_VALUE")
	private String value;
	
//	@Column(name="IS_MODIFIABLE")
//	private Boolean modifiable;//Can it be modified at runtime?
	
	@Column(name="DESCRIPTION")
	private String description;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
